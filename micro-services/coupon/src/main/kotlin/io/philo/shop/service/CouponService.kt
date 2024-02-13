package io.philo.shop.service

import io.philo.shop.domain.core.CouponRepository
import io.philo.shop.domain.core.UserCouponEntity
import io.philo.shop.domain.core.UserCouponRepository
import io.philo.shop.error.BadRequestException
import io.philo.shop.item.ItemRestClientFacade
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.service.CouponDiscountCalculator.Companion.calculateDiscountAmount
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class CouponService(
    private val itemClient: ItemRestClientFacade,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun createCoupon(): Unit {

//        couponRepository.save()
    }

    fun calculateAmountForInternal(userId: Long, reqUserCouponIds: List<Long>): Int {

        val itemDtos = itemClient.requestItems(reqUserCouponIds)

        val userCoupons = userCouponRepository.findAllUsable(userId, reqUserCouponIds)
        if(reqUserCouponIds.size != userCoupons.size)
            throw BadRequestException("사용자의 쿠폰에 대해 검증이 실패하였습니다.")

        return -1
    }

    /**
     * 다음의 사항들을 검증합니다.
     *
     * - 유저가 보유한 쿠폰 중에서 유효하지 않은 쿠폰이 있는지
     * - 할인한 금액이 맞는지 유저가 입력한 금액과 일치하는 지
     */
    fun checkCouponBeforeOrder(requesterId: Long, orderLineDtos: List<OrderLineCreatedEvent>): Boolean {

        for (orderLineDto in orderLineDtos) {

            val midVerification = iterCheckCouponBeforeOrder(orderLineDto, requesterId)
            if(midVerification.not()) return false
        }

        return true
    }

    private fun iterCheckCouponBeforeOrder(orderLineDto: OrderLineCreatedEvent, requesterId: Long): Boolean {

        val userCouponIds: List<Long>? = orderLineDto.userCouponIds
        if (userCouponIds.isNullOrEmpty()) {
            log.info { "쿠폰이 존재하지 않습니다." }
            return false
        }

        val foundUserCoupons: List<UserCouponEntity> =
            userCouponRepository.findAllUsable(requesterId, userCouponIds)
        if (userCouponIds.size != foundUserCoupons.size) {
            log.info { "유효하지 않은 쿠폰이 포함되어 있습니다." }
            return false
        }

        // todo item_replica와 비교하도록 설정하기
        val requestItemAmount = orderLineDto.itemAmount
        val requestDiscountedAmount = orderLineDto.itemDiscountedAmount
        val foundCoupons = foundUserCoupons.map { it.coupon }.toList()
        val actualDiscountedAmount = calculateDiscountAmount(requestItemAmount, foundCoupons)
        if (requestDiscountedAmount != actualDiscountedAmount) {
            log.info { "사용자의 요청 할인액이 실제 할인한 가격과 일치하지 않습니다." }
            return false
        }

        return true
    }

    fun useUserCoupons(requesterId: Long, orderLineEvents: List<OrderLineCreatedEvent>) {

        val userCouponIds = orderLineEvents.flatMap { it.userCouponIds!! }.toList()

        val userCoupons = userCouponRepository.findAllUsable(requesterId, userCouponIds)
        for (userCoupon in userCoupons) {
            userCoupon.useCoupon()
        }
    }
}