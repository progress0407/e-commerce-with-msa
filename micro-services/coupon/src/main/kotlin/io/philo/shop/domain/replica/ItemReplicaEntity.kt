package io.philo.shop.domain.replica

import io.philo.shop.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "item_replica")
class ItemReplicaEntity(

    @Column(nullable = false)
    val itemId: Long,

    @Column(nullable = false)
    val itemAmount: Int

) : BaseEntity() {

    constructor() : this(0L, 0)
}