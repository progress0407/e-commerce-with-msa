package io.philo.shop.repository

import io.philo.shop.domain.outbox.OrderCreatedOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderCreatedOutboxRepository : JpaRepository<OrderCreatedOutboxEntity, Long> {

    fun findAllByLoadedIsFalse(): List<OrderCreatedOutboxEntity>

    fun findByTraceId(orderId: Long): OrderCreatedOutboxEntity?

    @Query(
        """
        select o 
        from OrderCreatedOutboxEntity o
        where o.loaded = true
        and o.itemValidated <> io.philo.shop.common.VerificationStatus.PENDING 
        and o.couponValidated <> io.philo.shop.common.VerificationStatus.PENDING
    """
    )
    fun findAllToCompleteEvent(): List<OrderCreatedOutboxEntity>
}