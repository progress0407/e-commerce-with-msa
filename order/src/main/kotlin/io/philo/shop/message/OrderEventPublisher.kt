package io.philo.shop.message

import io.philo.shop.domain.OrderEntity
import io.philo.shop.domain.OrderLineItemEntity
import io.philo.shop.domain.OrderLineOutBox
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.order.OrderRabbitProperty.Companion.EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ROUTING_KEY
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class OrderEventPublisher(private val rabbitTemplate: RabbitTemplate) {

    /**
     * out box 에서 브로커로 이벤트 적재
     */
    fun publishEvent(outbox: OrderLineOutBox) {


        val event = OrderLineCreatedEvent.from(outbox)

        publishEventToBroker(event)
    }

    @Deprecated("OutBox 패턴 사용으로 인한 사용 중단")
    fun publishEvent(aggregateRoot: OrderEntity) {

        val event = OrderCreatedEvent.from(aggregateRoot)

        publishEventToBroker(event)
    }

    private fun publishEventToBroker(message: Any) =
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message)

    private fun processUserCouponIds(outbox: OrderLineOutBox): List<Long>? {

        val userCouponId1: Long? = outbox.userCouponId1
        val userCouponId2: Long? = outbox.userCouponId2

        if (userCouponId1 == null)
            return null

        val ids = mutableListOf<Long>()
        userCouponId1.let { ids += it }
        userCouponId2?.let { ids += it }

        return ids
    }

    private fun OrderLineCreatedEvent.Companion.from(outbox: OrderLineOutBox) =
        OrderLineCreatedEvent(
            itemId = outbox.itemId,
            itemAmount = outbox.itemAmount,
            itemDiscountedAmount = outbox.itemDiscountedAmount,
            itemQuantity = outbox.itemQuantity,
            userCouponIds = processUserCouponIds(outbox)
        )

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
