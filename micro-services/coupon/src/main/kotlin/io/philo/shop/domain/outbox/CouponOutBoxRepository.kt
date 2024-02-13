package io.philo.shop.domain.outbox

import org.springframework.data.jpa.repository.JpaRepository

interface CouponOutBoxRepository: JpaRepository<CouponOutBox, Long> {

    fun findAllByLoadedIsFalse(): List<CouponOutBox>
}