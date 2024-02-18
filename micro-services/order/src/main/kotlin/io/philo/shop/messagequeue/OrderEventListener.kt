package io.philo.shop.messagequeue

import io.philo.shop.CouponRabbitProperty.Companion.COUPON_VERIFY_RES_QUEUE_NAME
import io.philo.shop.application.OrderEventService
import io.philo.shop.common.OrderChangedVerifiedEvent
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_FAIL_RES_QUEUE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_RES_QUEUE_NAME
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class OrderEventListener(private val orderEventService: OrderEventService) {

    private val log = KotlinLogging.logger { }

    /**
     * 상품 검증 이벤트 수신처
     */
    @RabbitListener(queues = [ITEM_VERIFY_RES_QUEUE_NAME])
    fun listenItemVerification(event: OrderChangedVerifiedEvent) {

        orderEventService.processAfterItemVerification(event.orderId, event.verification)
    }

    /**
     * 쿠폰 검증 이벤트 수신처
     */
    @RabbitListener(queues = [COUPON_VERIFY_RES_QUEUE_NAME])
    fun listenCouponVerification(event: OrderChangedVerifiedEvent) {

        orderEventService.processAfterCouponVerification(event.orderId, event.verification)
    }

    @RabbitListener(queues = [ITEM_VERIFY_FAIL_RES_QUEUE_NAME])
    fun listenItemFailVerification(event: OrderChangedVerifiedEvent) {

        orderEventService.processAfterItemVerification(event.orderId, event.verification)
    }

}