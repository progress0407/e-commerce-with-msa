package io.philo.shop.presentation.dto

import io.philo.shop.domain.core.UserCouponEntity

data class UserCouponListDto(val id: Long, val userId: Long, val couponId:Long, val isUse: Boolean) {

    constructor(entity: UserCouponEntity) : this(entity.id!!, entity.userId, entity.coupon.id!!, entity.isUse)
}