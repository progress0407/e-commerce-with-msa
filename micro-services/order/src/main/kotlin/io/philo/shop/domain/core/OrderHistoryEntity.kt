package io.philo.shop.domain.core

import io.philo.shop.constant.OrderStatus
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "order_history")
class OrderHistoryEntity(

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    val orderEntity: OrderEntity,

    @Enumerated(STRING)
    @Column(nullable = false)
    val orderStatus: OrderStatus = OrderStatus.PENDING

) : BaseEntity() {
    protected constructor() : this(OrderEntity.empty())
}