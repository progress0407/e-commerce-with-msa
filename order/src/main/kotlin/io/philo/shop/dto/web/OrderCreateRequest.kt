package io.philo.shop.dto.web

import com.fasterxml.jackson.annotation.JsonCreator


data class OrderCreateRequest @JsonCreator constructor (
    val orderLineRequests: List<OrderLineRequest>
)