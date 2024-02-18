package io.philo.shop.domain.outbox

import io.philo.shop.entity.OutboxBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "coupon_outbox")
class CouponOutboxEntity(

    traceId: Long,

    requesterId: Long,

    @Column(nullable = false)
    val isCompensatingTx: Boolean = false, // 보상 트랜잭션 여부

    @Column(nullable = false)
    val verification: Boolean,

    ) : OutboxBaseEntity(traceId, requesterId) {

    protected constructor() : this(traceId = 0L, requesterId = 0L, verification = false)
}