package io.philo.shop.repository

import io.philo.shop.domain.outbox.OrderFailedOutBoxEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderFailedOutBoxRepository : JpaRepository<OrderFailedOutBoxEntity, Long> {
}