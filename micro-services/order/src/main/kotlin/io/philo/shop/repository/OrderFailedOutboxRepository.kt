package io.philo.shop.repository

import io.philo.shop.domain.outbox.OrderCreatedOutboxEntity
import io.philo.shop.domain.outbox.OrderFailedOutboxEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderFailedOutboxRepository : JpaRepository<OrderFailedOutboxEntity, Long> {
    fun findAllByLoadedIsFalse(): List<OrderFailedOutboxEntity>
    fun findByTraceId(orderId: Long): OrderFailedOutboxEntity?

    @Query(
        """
        select o 
        from OrderFailedOutboxEntity o
        where o.loaded = true
        and ( o.isCompensatingItem = false or ( o.isCompensatingItem = true and o.itemValidated <> io.philo.shop.common.VerificationStatus.SUCCESS )) 
        and ( o.isCompensatingCoupon = false or ( o.isCompensatingCoupon = true and o.couponValidated <> io.philo.shop.common.VerificationStatus.SUCCESS ))
    """
    )
    fun findAllToCompleteEvent(): List<OrderFailedOutboxEntity>
}