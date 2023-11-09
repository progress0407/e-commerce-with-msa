package msa.with.ddd.item.consumer.dto


class OrderCreatedEvent {

    private val itemIdToDecreaseQuantity: Map<Long, Int> = TODO()

    fun values(): Map<Long, Int> {
        return itemIdToDecreaseQuantity
    }
}