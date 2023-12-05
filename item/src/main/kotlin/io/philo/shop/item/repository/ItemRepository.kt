package io.philo.shop.item.repository

import io.philo.shop.item.domain.entity.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    
    fun findByIdIn(itemIds: Collection<Long>): List<Item>
}