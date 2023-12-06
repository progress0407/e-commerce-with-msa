package io.philo.shop.dto.web

import io.philo.shop.domain.Order

data class OrderListResponse(val id:Long, val totalOrderAmount: Int) {

    constructor(entity: Order) : this(entity.id!!, entity.totalOrderAmount)
}
