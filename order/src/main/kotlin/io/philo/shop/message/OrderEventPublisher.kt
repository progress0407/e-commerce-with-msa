package io.philo.shop.message

import io.philo.shop.domain.Order
import io.philo.shop.domain.OrderItem
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.RabbitConfig
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class OrderEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    fun publishEvent(aggregateRoot: Order) {

        val event = OrderCreatedEvent.from(aggregateRoot)

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, event)
    }

    private fun OrderCreatedEvent.Companion.from(order: Order): OrderCreatedEvent {

        val orderItems = order.orderItems

        return OrderCreatedEvent(orderItems.toMap())
    }

    /**
     * 주문 ID -> 주문 수량
     */
    private fun List<OrderItem>.toMap(): Map<Long, Int> {

        return this.associateBy({ it.itemId }, { it.orderedQuantity })
    }
}