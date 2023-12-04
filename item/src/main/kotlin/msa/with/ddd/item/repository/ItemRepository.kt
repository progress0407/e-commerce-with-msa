package msa.with.ddd.item.repository

import msa.with.ddd.item.domain.entity.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    
    fun findByIdIn(itemIds: Collection<Long>): List<Item>
}