package io.philo.shop.domain.outbox

import org.springframework.data.jpa.repository.JpaRepository

interface CouponOutBoxRepository: JpaRepository<CouponOutboxEntity, Long> {

    fun findAllByLoadedIsFalse(): List<CouponOutboxEntity>
}