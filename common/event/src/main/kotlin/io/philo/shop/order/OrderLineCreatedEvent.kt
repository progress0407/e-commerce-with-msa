package io.philo.shop.order

data class OrderLineCreatedEvent(
    val itemId : Long,
    val itemAmount: Int,
    val itemDiscountedAmount: Int,
    val itemQuantity: Int,
    val userCouponIds: List<Long>?
)
