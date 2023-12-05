package io.philo.shop.order.dto.web

import io.philo.shop.order.domain.Order

data class OrderListResponse(val id:Long, val totalOrderAmount: Int) {

    constructor(entity: Order) : this(entity.id!!, entity.totalOrderAmount)
}
