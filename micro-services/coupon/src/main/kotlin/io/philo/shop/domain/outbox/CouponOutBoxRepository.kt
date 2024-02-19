package io.philo.shop.domain.outbox

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CouponOutBoxRepository: JpaRepository<CouponOutboxEntity, Long> {

    fun findAllByLoadedIsFalse(): List<CouponOutboxEntity>

    @Query("""
        select ob 
        from CouponOutboxEntity ob
        where ob.loaded = false 
        and ob.isCompensatingTx = false 
    """)
    fun findAllToNormalTx(): List<CouponOutboxEntity>

    @Query("""
        select ob 
        from CouponOutboxEntity ob
        where ob.loaded = false 
        and ob.isCompensatingTx = true 
    """)
    fun findAllToCompensatingTx(): List<CouponOutboxEntity>
}