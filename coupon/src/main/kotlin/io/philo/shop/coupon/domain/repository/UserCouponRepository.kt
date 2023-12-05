package io.philo.shop.coupon.domain.repository

import io.philo.shop.coupon.domain.entity.UserCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponRepository : JpaRepository<UserCoupon, Long> {
}