package io.philo.shop.repository

import io.philo.shop.domain.outbox.OrderCreatedOutBoxEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderCreatedOutBoxRepository : JpaRepository<OrderCreatedOutBoxEntity, Long> {

    fun findAllByLoadedIsFalse(): List<OrderCreatedOutBoxEntity>

    fun findByOrderId(orderId: Long): OrderCreatedOutBoxEntity?

    @Query(
        """
        select o from OrderCreatedOutBoxEntity o
        where o.loaded = true
        and o.itemValidated in (io.philo.shop.common.VerificationStatus.SUCCESS, io.philo.shop.common.VerificationStatus.FAIL) 
        and o.couponValidated in (io.philo.shop.common.VerificationStatus.SUCCESS, io.philo.shop.common.VerificationStatus.FAIL)
    """
    )
    fun findAllToCompleteEvent(): List<OrderCreatedOutBoxEntity>
}