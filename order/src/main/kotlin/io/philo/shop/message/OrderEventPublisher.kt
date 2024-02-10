package io.philo.shop.message

import io.philo.shop.domain.OrderEntity
import io.philo.shop.domain.OrderLineItemEntity
import io.philo.shop.domain.OrderLineOutBox
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.order.RabbitConfig
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class OrderEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    fun publishEvent(aggregateRoot: OrderEntity) {

        val event = OrderCreatedEvent.from(aggregateRoot)

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, event)
    }

    /**
     * out box 에서 브로커로 이벤트 적재
     */
    fun publishEvent(outbox: OrderLineOutBox) {

        OrderLineCreatedEvent(
            itemId = outbox.itemId,
            itemAmount = outbox.itemAmount,
            itemDiscountedAmount = outbox.itemDiscountedAmount,
            itemQuantity = outbox.itemQuantity,
            userCouponIds = processUserCouponIds(outbox)
        )
    }

    private fun processUserCouponIds(outbox: OrderLineOutBox): List<Long>? {

        val userCouponId1: Long? = outbox.userCouponId1
        val userCouponId2: Long? = outbox.userCouponId2

        if(userCouponId1 == null)
            return null

        val ids = mutableListOf<Long>()
        userCouponId1.let { ids += it }
        userCouponId2?.let { ids += it }

        return ids
    }

    private fun OrderCreatedEvent.Companion.from(orderEntity: OrderEntity): OrderCreatedEvent {

        val orderItems = orderEntity.orderLineItemEntities

        return OrderCreatedEvent(orderItems.toMap())
    }

    /**
     * 주문 ID -> 주문 수량
     */
    private fun List<OrderLineItemEntity>.toMap(): Map<Long, Int> {

        return this.associateBy({ it.itemId }, { it.orderedQuantity })
    }
}