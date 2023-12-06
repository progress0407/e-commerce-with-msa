package io.philo.shop.consumer.dto


class OrderCreatedEvent {

    private val itemIdToDecreaseQuantity: Map<Long, Int> = TODO()

    fun values(): Map<Long, Int> {
        return itemIdToDecreaseQuantity
    }
}