package io.philo.shop.domain.outbox

import jakarta.persistence.*

@Entity
class ItemOutBox(

    @Column(nullable = false)
    val orderId: Long,

    @Column(nullable = false)
    val verification: Boolean
) {
    constructor() : this(-1L, false)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    private var loaded: Boolean = false // 발송 여부 // todo! loaded 리펙터링

    fun load() {
        this.loaded = true
    }
}