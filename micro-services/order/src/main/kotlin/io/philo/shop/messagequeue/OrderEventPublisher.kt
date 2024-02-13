package io.philo.shop.messagequeue

import io.philo.shop.common.InAppEventPublisher
import io.philo.shop.domain.core.OrderEntity
import io.philo.shop.domain.core.OrderLineItemEntity
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderCreatedEventDeprecated
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_ROUTING_KEY
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_ROUTING_KEY
import org.springframework.amqp.rabbit.core.RabbitTemplate

/**
 * RabbitMQ 브로커에 이벤트를 적재하는 역할
 */
@InAppEventPublisher
class OrderEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    fun publishEventToItemServer(event: OrderCreatedEvent) {

        rabbitTemplate.convertAndSend(ORDER_CREATED_TO_ITEM_EXCHANGE_NAME, ORDER_CREATED_TO_ITEM_ROUTING_KEY, event)
    }

    fun publishEventToCouponServer(event: OrderCreatedEvent) {

        rabbitTemplate.convertAndSend(ORDER_CREATED_TO_COUPON_EXCHANGE_NAME, ORDER_CREATED_TO_COUPON_ROUTING_KEY, event)
    }


    @Deprecated("OutBox 패턴 사용으로 인한 사용 중단")
    fun publishEvent(aggregateRoot: OrderEntity) {

        val event = OrderCreatedEventDeprecated.from(aggregateRoot)

        rabbitTemplate.convertAndSend(ORDER_CREATED_TO_ITEM_EXCHANGE_NAME, ORDER_CREATED_TO_ITEM_ROUTING_KEY, event)
    }

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
