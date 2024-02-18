package io.philo.shop.repository

import io.philo.shop.domain.outbox.ItemOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemOutBoxRepository : JpaRepository<ItemOutboxEntity, Long> {

//    fun findAllByLoadedIsFalse(): List<ItemOutboxEntity>

    @Query("""
        select ob 
        from ItemOutboxEntity ob
        where ob.loaded = false 
        and ob.isCompensatingTx = false 
    """)
    fun findAllToNormalTx(): List<ItemOutboxEntity>

    @Query("""
        select ob 
        from ItemOutboxEntity ob
        where ob.loaded = false 
        and ob.isCompensatingTx = true 
    """)
    fun findAllToCompensatingTx(): List<ItemOutboxEntity>
}