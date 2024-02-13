package io.philo.shop.domain.outbox

import io.philo.shop.entity.OutboxBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "item_outbox")
class ItemOutboxEntity(

    traceId: Long,

    requesterId: Long,

    @Column(nullable = false)
    val verification: Boolean

) : OutboxBaseEntity(traceId, requesterId) {

    protected constructor() : this(0L, 0L, false)
}