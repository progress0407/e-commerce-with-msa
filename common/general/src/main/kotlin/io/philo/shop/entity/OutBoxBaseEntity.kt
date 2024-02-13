package io.philo.shop.entity

import jakarta.persistence.*

@MappedSuperclass
abstract class OutBoxBaseEntity(

    @Column(nullable = false)
    val traceId: Long, // 분산 트랜잭션에서의 고유 ID (이 프로젝트에서는 주로 orderId이다)

    @Column(nullable = false)
    val requesterId: Long

) :BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null

    @Column(nullable = false)
    private var loaded: Boolean = false // 발송 여부

    fun load() {
        this.loaded = true
    }
}