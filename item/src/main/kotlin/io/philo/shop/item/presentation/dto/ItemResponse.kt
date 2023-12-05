package io.philo.shop.item.presentation.dto

import io.philo.shop.item.domain.entity.Item

data class ItemResponse(
    val id: Long,
    val name: String,
    val size: String,
    val price: Int,
    val availableQuantity: Int
) {


    constructor(item: Item) : this(item.id!!, item.name, item.size, item.price, item.stockQuantity)
}