package io.philo.shop.dto.event

import io.philo.shop.domain.OrderItem
import java.util.stream.Collectors.toMap

class OrderCreatedEvent(orderItems: List<OrderItem>) {

    private val itemIdToDecreaseQuantity: Map<Long, Int> = createInnerDto(orderItems)

    fun values(): Map<Long, Int> {
        return itemIdToDecreaseQuantity
    }

    companion object {
        private fun createInnerDto(orderItems: List<OrderItem>): Map<Long, Int> {
            return orderItems.stream()
                .collect(toMap({ it.itemId }, { it.orderItemQuantity }))
        }
    }
}