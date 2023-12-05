package io.philo.shop.order.dto.web

import com.fasterxml.jackson.annotation.JsonCreator

data class ItemResponses @JsonCreator constructor(val items: List<ItemResponse>)