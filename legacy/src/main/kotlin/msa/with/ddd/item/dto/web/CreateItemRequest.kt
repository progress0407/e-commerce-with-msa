package msa.with.ddd.item.dto.web

data class CreateItemRequest(
    val name: String = "",
    val size: String = "",
    val price: Int = 0,
    val availableQuantity: Int = 0
)
