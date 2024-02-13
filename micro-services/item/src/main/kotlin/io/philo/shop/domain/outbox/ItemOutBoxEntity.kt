package io.philo.shop.domain.outbox

import io.philo.shop.entity.OutBoxBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class ItemOutBoxEntity(

    traceId: Long,

    requesterId: Long,

    @Column(nullable = false)
    val verification: Boolean

) : OutBoxBaseEntity(traceId, requesterId) {

    protected constructor() : this(0L, 0L, false)
}