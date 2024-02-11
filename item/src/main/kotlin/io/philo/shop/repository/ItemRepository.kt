package io.philo.shop.repository

import io.philo.shop.domain.entity.ItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<ItemEntity, Long> {
    
    fun findByIdIn(itemIds: Collection<Long>): List<ItemEntity>
}