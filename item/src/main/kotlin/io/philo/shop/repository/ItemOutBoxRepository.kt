package io.philo.shop.repository

import io.philo.shop.domain.outbox.ItemOutBox
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOutBoxRepository : JpaRepository<ItemOutBox, Long> {

    fun findAllByLoadedIsFalse(): List<ItemOutBox>
}