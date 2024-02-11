package io.philo.shop.item

data class ItemVerificationEvent(val orderId: Long, val verification: Boolean)
