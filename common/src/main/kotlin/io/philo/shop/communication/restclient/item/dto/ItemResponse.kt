package io.philo.shop.communication.restclient.item.dto

data class ItemResponse(
    val id: Long,
    val name: String,
    val amount: Int
)
