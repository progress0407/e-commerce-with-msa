package io.philo.shop.domain.entity.item

import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*

@Entity
class ItemReplicaEntity(

    @Column(nullable = false)
    val itemId: Long,

    @Column(nullable = false)
    val itemAmount: Int

) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    constructor() : this(0L, 0)
}