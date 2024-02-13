package io.philo.shop.domain.core

import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<CouponEntity, Long> {

    fun findAllByIdIn(ids: List<Long>): List<CouponEntity>
}