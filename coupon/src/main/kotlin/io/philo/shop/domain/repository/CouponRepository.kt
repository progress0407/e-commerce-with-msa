package io.philo.shop.domain.repository

import io.philo.shop.domain.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long> {

    fun findAllByIdIn(ids: List<Long>): List<Coupon>
}