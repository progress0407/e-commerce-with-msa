package io.philo.shop.repository

import io.philo.shop.domain.outbox.ItemOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOutBoxRepository : JpaRepository<ItemOutboxEntity, Long> {

    fun findAllByLoadedIsFalse(): List<ItemOutboxEntity>
}