package io.philo.shop.dto.web

import io.philo.shop.domain.core.OrderEntity

data class OrderListResponse(val id:Long, val totalOrderAmount: Int) {

    constructor(entity: OrderEntity) : this(entity.id!!, entity.totalOrderAmount)
}
