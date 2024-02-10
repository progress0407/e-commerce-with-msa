package io.philo.shop.repository

import io.philo.shop.domain.OrderLineOutBox
import org.springframework.data.jpa.repository.JpaRepository

interface OrderOutBoxTableRepository: JpaRepository<OrderLineOutBox, Long> {

    fun findAllByLoadedIsFalse(): List<OrderLineOutBox>
}