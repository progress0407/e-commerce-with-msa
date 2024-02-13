package io.philo.shop.order

data class OrderCreatedEvent(
    val orderId : Long,
    val requesterId: Long,
    val orderLineCreatedEvents: List<OrderLineCreatedEvent>
) {
    companion object
}
