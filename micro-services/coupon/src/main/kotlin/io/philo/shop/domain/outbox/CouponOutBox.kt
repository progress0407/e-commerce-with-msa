package io.philo.shop.domain.outbox

import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*

@Entity
class CouponOutBox(

    @Column(nullable = false)
    val orderId: Long,

    @Column(nullable = false)
    val verification: Boolean
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null


    @Column(nullable = false)
    private var loaded: Boolean = false // 발송 여부 // todo! loaded 리펙터링

    fun load() {
        this.loaded = true
    }

    constructor() : this(0L, false)
}