package io.philo.shop.domain.repository

import io.philo.shop.domain.entity.UserCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponRepository : JpaRepository<UserCoupon, Long> {

    fun findByUserId(userId: Long)
    fun findAllByUserIdAndCouponIdIn(userId: Long, couponIds:List<Long>): List<UserCoupon>
}