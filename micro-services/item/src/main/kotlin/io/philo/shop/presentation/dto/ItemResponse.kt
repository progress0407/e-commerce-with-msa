package io.philo.shop.presentation.dto

import io.philo.shop.domain.entity.ItemEntity

data class ItemResponse(
    val id: Long,
    val name: String,
    val size: String,
    val price: Int,
    val availableQuantity: Int
) {

    constructor(itemEntity: ItemEntity) : this(itemEntity.id!!, itemEntity.name, itemEntity.size, itemEntity.price, itemEntity.stockQuantity)
}