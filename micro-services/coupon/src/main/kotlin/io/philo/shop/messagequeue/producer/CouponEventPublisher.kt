package io.philo.shop.messagequeue.producer

import io.philo.shop.CouponRabbitProperty.Companion.COUPON_VERIFY_RES_EXCHANGE_NAME
import io.philo.shop.CouponRabbitProperty.Companion.COUPON_VERIFY_RES_ROUTING_KEY
import io.philo.shop.common.InAppEventPublisher
import io.philo.shop.common.OrderCreatedVerifiedEvent
import org.springframework.amqp.rabbit.core.RabbitTemplate

@InAppEventPublisher
class CouponEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    /**
     * 주문생성시 검증 요청한 값을 전송
     */
    fun publishEvent(event: OrderCreatedVerifiedEvent) {

        publishEventToBroker(event)
    }

    private fun publishEventToBroker(event: OrderCreatedVerifiedEvent) {

        rabbitTemplate.convertAndSend(COUPON_VERIFY_RES_EXCHANGE_NAME, COUPON_VERIFY_RES_ROUTING_KEY, event)
    }
}