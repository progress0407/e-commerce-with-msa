package msa.with.ddd.item.repository

import msa.with.ddd.item.domain.Item
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface ItemRepository : JpaRepository<Item, Long> {
    
    @Lock(value = LockModeType.OPTIMISTIC)
    fun findByIdIn(itemIds: Collection<Long>): List<Item>
}