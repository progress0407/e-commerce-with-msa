package io.philo.shop.messagequeue.producer

import io.philo.shop.common.InAppEventPublisher
import io.philo.shop.common.OrderCreatedVerifiedEvent
import io.philo.shop.domain.entity.ItemEntity
import io.philo.shop.item.ItemCreatedEvent
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_RES_EXCHANGE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_RES_ROUTING_KEY
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_RES_EXCHANGE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_RES_ROUTING_KEY
import org.springframework.amqp.rabbit.core.RabbitTemplate

@InAppEventPublisher
class ItemEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    /**
     * 주문생성시 검증 요청한 값을 전송
     */
    fun publishEvent(event: OrderCreatedVerifiedEvent) {

        publishEventToBroker(event)
    }

    fun publishEvent(event: ItemCreatedEvent) {

        rabbitTemplate.convertAndSend(ITEM_REPLICA_FOR_COUPON_RES_EXCHANGE_NAME, ITEM_REPLICA_FOR_COUPON_RES_ROUTING_KEY, event)
    }

    private fun publishEventToBroker(message: Any) =
        rabbitTemplate.convertAndSend(ITEM_VERIFY_RES_EXCHANGE_NAME, ITEM_VERIFY_RES_ROUTING_KEY, message)


    private fun publishEventToBroker(message: ItemCreatedEvent, routingKey: String) =
        rabbitTemplate.convertAndSend(routingKey, message)
}

fun ItemEntity.toEvent() = ItemCreatedEvent(this.id!!, this.price)
