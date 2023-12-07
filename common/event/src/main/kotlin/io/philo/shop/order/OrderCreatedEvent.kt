package io.philo.shop.order

data class OrderCreatedEvent(val itemIdToOrderedQuantity: Map<Long, Int>) {

    fun values(): Map<Long, Int> {
        return itemIdToOrderedQuantity
    }

    /**
     * this constructor for json se/deserialize
     */
    constructor(): this(emptyMap())

    companion object
}