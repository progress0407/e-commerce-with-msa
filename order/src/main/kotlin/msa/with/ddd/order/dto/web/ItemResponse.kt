package msa.with.ddd.order.dto.web

import com.fasterxml.jackson.annotation.JsonCreator

data class ItemResponse @JsonCreator constructor(
    val id: Long,
    val name: String,
    val size: String,
    val price: Int,
    val availableQuantity: Int
)