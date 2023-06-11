package msa.with.ddd.item.dto.order


class OrderCreatedEvent {

    private val itemIdToDecreaseQuantity: Map<Long, Int> = TODO()

    fun values(): Map<Long, Int> {
        return itemIdToDecreaseQuantity
    }
}