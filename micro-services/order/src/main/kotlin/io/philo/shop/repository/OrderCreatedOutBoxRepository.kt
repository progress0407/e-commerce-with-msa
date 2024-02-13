package io.philo.shop.repository

import io.philo.shop.domain.outbox.OrderCreatedOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderCreatedOutBoxRepository : JpaRepository<OrderCreatedOutboxEntity, Long> {

    fun findAllByLoadedIsFalse(): List<OrderCreatedOutboxEntity>

    fun findByTraceId(orderId: Long): OrderCreatedOutboxEntity?

    @Query(
        """
        select o 
        from OrderCreatedOutboxEntity o
        where o.loaded = true
        and o.itemValidated in (io.philo.shop.common.VerificationStatus.SUCCESS, io.philo.shop.common.VerificationStatus.FAIL) 
        and o.couponValidated in (io.philo.shop.common.VerificationStatus.SUCCESS, io.philo.shop.common.VerificationStatus.FAIL)
    """
    )
    fun findAllToCompleteEvent(): List<OrderCreatedOutboxEntity>
}