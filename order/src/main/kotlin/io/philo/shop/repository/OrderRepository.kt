package io.philo.shop.repository

import io.philo.shop.domain.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrderEntity, Long>