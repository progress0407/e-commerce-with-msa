package io.philo.shop.messagequeue.consumer

import io.philo.shop.common.InAppEventListener
import io.philo.shop.domain.outbox.CouponOutBoxRepository
import io.philo.shop.domain.replica.ItemReplicaRepository
import io.philo.shop.item.ItemCreatedEvent
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_QUEUE_NAME
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_QUEUE_NAME
import io.philo.shop.service.CouponEventService
import io.philo.shop.service.CouponService
import io.philo.shop.service.toEntity
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener

@InAppEventListener
class CouponEventListener(
    private val couponService: CouponService,
    private val itemReplicaRepository: ItemReplicaRepository,
    private val couponOutBoxRepository: CouponOutBoxRepository,
    private val couponEventService: CouponEventService
) {

    private val log = KotlinLogging.logger { }

    /**
     * 주문 생성 이벤트 수신처
     */
    @RabbitListener(queues = [ORDER_CREATED_TO_COUPON_QUEUE_NAME])
    fun listenOrderCreatedEvent(event: OrderCreatedEvent) {

        couponEventService.validateAndProcessCoupon(event)
    }

    /**
     * 상품의 복제본 동기화
     */
    @RabbitListener(queues = [ITEM_REPLICA_FOR_COUPON_QUEUE_NAME])
    fun listenItemReplicaForCoupon(event: ItemCreatedEvent) {

        val entity = event.toEntity()
        itemReplicaRepository.save(entity)
    }
}



