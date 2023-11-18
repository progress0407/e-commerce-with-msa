package io.philo.domain.entity

class UserCoupon(
    val id: Long?,
    val userId: Long,
    val couponId: Long,
    var isUse: Boolean
) {
}