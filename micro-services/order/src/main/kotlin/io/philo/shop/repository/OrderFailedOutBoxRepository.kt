package io.philo.shop.repository

import io.philo.shop.domain.outbox.OrderFailedOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderFailedOutBoxRepository : JpaRepository<OrderFailedOutboxEntity, Long> {
}