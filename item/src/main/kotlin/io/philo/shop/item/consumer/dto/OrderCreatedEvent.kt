package io.philo.shop.item.consumer.dto


class OrderCreatedEvent {

    private val itemIdToDecreaseQuantity: Map<Long, Int> = TODO()

    fun values(): Map<Long, Int> {
        return itemIdToDecreaseQuantity
    }
}