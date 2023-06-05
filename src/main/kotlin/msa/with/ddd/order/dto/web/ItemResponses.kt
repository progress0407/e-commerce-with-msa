package msa.with.ddd.order.dto.web

import com.fasterxml.jackson.annotation.JsonCreator

data class ItemResponses @JsonCreator constructor(val items: List<ItemResponse>)