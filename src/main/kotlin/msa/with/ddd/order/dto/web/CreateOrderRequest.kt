package msa.with.ddd.order.dto.web

import com.fasterxml.jackson.annotation.JsonCreator


data class CreateOrderRequest @JsonCreator constructor (
    val orderLineRequests: List<OrderLineRequest>
)