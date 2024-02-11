package io.philo.shop.repository

import io.philo.shop.domain.OrderOutBox
import org.springframework.data.jpa.repository.JpaRepository

interface OrderOutBoxRepository: JpaRepository<OrderOutBox, Long> {

    fun findAllByLoadedIsFalse(): List<OrderOutBox>
}