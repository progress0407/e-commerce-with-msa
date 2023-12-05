package io.philo.shop.coupon.domain.repository

import io.philo.shop.coupon.domain.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long> {

    fun findAllByIdIn(ids: List<Long>): List<Coupon>
}