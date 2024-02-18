package io.philo.shop.service

import io.philo.shop.common.OrderChangedVerifiedEvent
import io.philo.shop.domain.core.CouponRepository
import io.philo.shop.domain.core.UserCouponEntity
import io.philo.shop.domain.core.UserCouponRepository
import io.philo.shop.domain.outbox.CouponOutBoxRepository
import io.philo.shop.domain.outbox.CouponOutboxEntity
import io.philo.shop.domain.replica.ItemReplicaEntity
import io.philo.shop.domain.replica.ItemReplicaRepository
import io.philo.shop.entity.toMap
import io.philo.shop.item.ItemCreatedEvent
import io.philo.shop.messagequeue.producer.CouponEventPublisher
import io.philo.shop.order.OrderChangedEvent
import io.philo.shop.order.OrderLineCreatedEvent
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponEventService(
    private val couponOutBoxRepository: CouponOutBoxRepository,
    private val couponRepository: CouponRepository,
    private val itemReplRepository: ItemReplicaRepository,
    private val userCouponRepository: UserCouponRepository,
    private val couponEventPublisher: CouponEventPublisher,
) {


    private val log = KotlinLogging.logger { }


    /**
     * 쿠폰에 대한 유효성 검증을 하고 이상이 없을 경우 쿠폰을 사용합니다.
     */
    @Transactional
    fun validateAndProcessOrderCreatedEvent(event: OrderChangedEvent) {

        val orderLineEvents = event.orderLineCreatedEvents
        val requesterId = event.requesterId

        val couponVerification = checkCouponForOrder(requesterId, orderLineEvents)
        if (couponVerification) {
            changeUserCoupons(requesterId, orderLineEvents) { userCoupon -> userCoupon.useCoupon() }
        }

        val outbox = CouponOutboxEntity(event.orderId, event.requesterId, false, couponVerification)
        couponOutBoxRepository.save(outbox)
    }

    /**
     * 실패할 이벤트에 대해 쿠폰을 다시 사용 가능 상태로 되돌립니다.
     */
    @Transactional
    fun processOrderFailedEvent(event: OrderChangedEvent) {

        val orderLineEvents = event.orderLineCreatedEvents
        val requesterId = event.requesterId

        changeUserCoupons(requesterId, orderLineEvents) { userCoupon -> userCoupon.changeToUsable() }
    }

    @Transactional
    fun loadEventToBroker() {

        val outboxes = couponOutBoxRepository.findAllByLoadedIsFalse()

        if (outboxes.isNullOrEmpty())
            return

        log.info { "브로커에 적재할 이벤트가 존재합니다." }

        val outBoxMap = outboxes.toMap()
        val events = outboxes.map { OrderChangedVerifiedEvent(it.traceId, it.verification) }.toList()

        for (event in events) {
            couponEventPublisher.publishEvent(event)

            val matchedOutBox = outBoxMap[event.orderId]!!
            matchedOutBox.load()
        }
    }

    /**
     * 다음의 사항들을 검증합니다.
     *
     * - 유저가 보유한 쿠폰 중에서 유효하지 않은 쿠폰이 있는지
     * - 할인한 금액이 맞는지 유저가 입력한 금액과 일치하는 지
     */
    private fun checkCouponForOrder(requesterId: Long, orderLineDtos: List<OrderLineCreatedEvent>): Boolean {

        for (orderLineDto in orderLineDtos) {

            val midVerification = iterCheckCouponForOrder(orderLineDto, requesterId)
            if (midVerification.not()) return false
        }

        return true
    }

    private fun iterCheckCouponForOrder(orderLineDto: OrderLineCreatedEvent, requesterId: Long): Boolean {

        val userCouponIds: List<Long>? = orderLineDto.userCouponIds
        if (userCouponIds.isNullOrEmpty()) {
            log.info { "쿠폰이 존재하지 않습니다." }
            return false
        }

        val foundUserCoupons = userCouponRepository.findAllUsable(requesterId, userCouponIds)
        if (userCouponIds.size != foundUserCoupons.size) {
            log.info { "유효하지 않은 쿠폰이 포함되어 있습니다." }
            return false
        }

        // todo item_replica와 비교하도록 설정하기
        val requestItemAmount = orderLineDto.itemAmount
        val requestDiscountedAmount = orderLineDto.itemDiscountedAmount
        val foundCoupons = foundUserCoupons.map { it.coupon }.toList()
        val actualDiscountedAmount = CouponDiscountCalculator.calculateDiscountAmount(requestItemAmount, foundCoupons)
        if (requestDiscountedAmount != actualDiscountedAmount) {
            log.info { "사용자의 요청 할인액이 실제 할인한 가격과 일치하지 않습니다." }
            return false
        }

        return true
    }

    private fun changeUserCoupons(
        requesterId: Long,
        orderLineEvents: List<OrderLineCreatedEvent>,
        couponStatusChanger: (UserCouponEntity) -> Unit,
    ) {

        val userCouponIds = orderLineEvents.flatMap { it.userCouponIds!! }.toList()
        val userCoupons = userCouponRepository.findAllUsable(requesterId, userCouponIds)
        for (userCoupon in userCoupons) {

            couponStatusChanger.invoke(userCoupon)
        }
    }

}

fun ItemCreatedEvent.toEntity() = ItemReplicaEntity(this.id, this.amount)
