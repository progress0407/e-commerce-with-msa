package io.philo.shop.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class OutboxBaseEntity(

    @Column(nullable = false)
    val traceId: Long, // 분산 트랜잭션에서의 고유 ID (이 프로젝트에서는 주로 orderId이다)

    @Column(nullable = false)
    val requesterId: Long,

    ) : BaseEntity() {

    @Column(nullable = false)
    private var loaded: Boolean = false // 발송 여부

    fun load() {
        this.loaded = true
    }
}

/**
 * ID -> Entity 에 대응하는 Map을 만든다
 */
fun List<OutboxBaseEntity>.toMap(): Map<Long, OutboxBaseEntity> =
    this.associateBy { it.traceId }