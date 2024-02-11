package io.philo.shop.queue.producer

import io.philo.shop.item.ItemRabbitProperty
import io.philo.shop.item.ItemVerificationEvent
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class ItemEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    /**
     * 주문생성시 검증 요청한 값을 전송
     */
    fun publishEvent(itemVerification: ItemVerificationEvent) {

        publishEventToBroker(itemVerification)
    }

    private fun publishEventToBroker(message: Any) =
        rabbitTemplate.convertAndSend(ItemRabbitProperty.ITEM_VERIFY_REQ_EXCHANGE_NAME, ItemRabbitProperty.ITEM_VERIFY_REQ_ROUTING_KEY, message)
}