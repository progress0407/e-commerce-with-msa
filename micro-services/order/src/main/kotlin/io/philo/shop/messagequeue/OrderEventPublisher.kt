package io.philo.shop.messagequeue

import io.philo.shop.domain.core.OrderEntity
import io.philo.shop.domain.core.OrderLineItemEntity
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderCreatedEventDeprecated
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_ROUTING_KEY
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class OrderEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    /**
     * out box 에서 브로커로 이벤트 적재
     */
    fun publishEvent(event: OrderCreatedEvent) {

        publishEventToBroker(event)
    }

    @Deprecated("OutBox 패턴 사용으로 인한 사용 중단")
    fun publishEvent(aggregateRoot: OrderEntity) {

        val event = OrderCreatedEventDeprecated.from(aggregateRoot)

        publishEventToBroker(event)
    }

    private fun publishEventToBroker(message: Any) =
        rabbitTemplate.convertAndSend(ORDER_CREATED_EXCHANGE_NAME, ORDER_CREATED_ROUTING_KEY, message)

    private fun OrderCreatedEventDeprecated.Companion.from(orderEntity: OrderEntity): OrderCreatedEventDeprecated {

        val orderItems = orderEntity.orderLineItemEntities

        return OrderCreatedEventDeprecated(orderItems.toMap())
    }

    /**
     * 주문 ID -> 주문 수량
     */
    private fun List<OrderLineItemEntity>.toMap(): Map<Long, Int> {

        return this.associateBy({ it.itemId }, { it.orderedQuantity })
    }
}
