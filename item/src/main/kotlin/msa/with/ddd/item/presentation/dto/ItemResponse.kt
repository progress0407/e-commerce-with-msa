package msa.with.ddd.item.presentation.dto

import msa.with.ddd.item.domain.entity.Item

data class ItemResponse(
    val id: Long,
    val name: String,
    val size: String,
    val price: Int,
    val availableQuantity: Int
) {


    constructor(item: Item) : this(item.id!!, item.name, item.size, item.price, item.stockQuantity)
}