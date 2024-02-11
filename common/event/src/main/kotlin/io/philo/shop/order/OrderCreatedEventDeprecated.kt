package io.philo.shop.order

@Deprecated("OutBox 패턴으로 사용중단")
data class OrderCreatedEventDeprecated(val itemIdToOrderedQuantity: Map<Long, Int>) {

    fun values(): Map<Long, Int> {
        return itemIdToOrderedQuantity
    }

    /**
     * this constructor for json se/deserialize
     */
    constructor(): this(emptyMap())

    companion object
}