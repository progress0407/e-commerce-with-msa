package io.philo.shop.repository

import io.philo.shop.domain.outbox.ItemOutBoxEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOutBoxRepository : JpaRepository<ItemOutBoxEntity, Long> {

    fun findAllByLoadedIsFalse(): List<ItemOutBoxEntity>
}