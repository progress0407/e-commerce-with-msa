package io.philo.shop.domain.repository

import io.philo.shop.domain.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<CouponEntity, Long> {

    fun findAllByIdIn(ids: List<Long>): List<CouponEntity>
}