package msa.with.ddd.order.dto.web

import msa.with.ddd.order.domain.Order

data class OrderListResponse(val id:Long, val totalOrderAmount: Int) {

    constructor(entity: Order) : this(entity.id!!, entity.totalOrderAmount)
}
