package io.philo.shop.messagequeue.consumer

import io.philo.shop.domain.outbox.CouponOutBox
import io.philo.shop.domain.outbox.CouponOutBoxRepository
import io.philo.shop.domain.replica.ItemReplicaEntity
import io.philo.shop.domain.replica.ItemReplicaRepository
import io.philo.shop.item.ItemCreatedEvent
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_QUEUE_NAME
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderRabbitProperty
import io.philo.shop.service.CouponService
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class CouponEventListener(
    private val couponService: CouponService,
    private val itemReplicaRepository: ItemReplicaRepository,
    private val couponOutBoxRepository: CouponOutBoxRepository
) {

    private val log = KotlinLogging.logger { }

    /**
     * 상품의 복제본 동기화
     */
    @RabbitListener(queues = [ITEM_REPLICA_FOR_COUPON_QUEUE_NAME])
    fun listenItemReplicaForCoupon(event: ItemCreatedEvent) {

        val entity = event.toEntity()
        itemReplicaRepository.save(entity)
    }

    /**
     * 주문 생성 이벤트 수신처
     */
    @RabbitListener(queues = [OrderRabbitProperty.ORDER_CREATED_QUEUE_NAME])
    fun listenOrderCreatedEvent(event: OrderCreatedEvent) {

        log.info { "$event" }

        val orderLineEvents = event.orderLineCreatedEvents
        val requesterId = event.requesterId

        val couponVerification = couponService.checkCouponBeforeOrder(requesterId, orderLineEvents)
        if (couponVerification) {
            couponService.useUserCoupons(requesterId, orderLineEvents)
        }

        val outbox = CouponOutBox(event.orderId, event.requesterId, couponVerification)
        couponOutBoxRepository.save(outbox)
    }
}

private fun ItemCreatedEvent.toEntity() = ItemReplicaEntity(this.id, this.amount)

