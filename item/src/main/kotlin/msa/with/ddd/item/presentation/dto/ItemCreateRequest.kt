package msa.with.ddd.item.presentation.dto

data class ItemCreateRequest(
    val name: String = "",
    val size: String = "",
    val price: Int = 0,
    val availableQuantity: Int = 0
)
