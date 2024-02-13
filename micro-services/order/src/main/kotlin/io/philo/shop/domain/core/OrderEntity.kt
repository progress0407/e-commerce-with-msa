package io.philo.shop.domain.core

import io.philo.shop.constant.OrderStatus
import io.philo.shop.constant.OrderStatus.*
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.FetchType.EAGER
import lombok.ToString

@Entity
@Table(name = "orders")
@ToString(exclude = ["orderItems"])
class OrderEntity(

    val requesterId: Long,

    @OneToMany(mappedBy = "orderEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = EAGER)
    var orderLineItemEntities: MutableList<OrderLineItemEntity> = mutableListOf()

) : BaseEntity() {

    @Column(nullable = false)
    var totalOrderAmount: Int = 0

    @Enumerated(STRING)
    @Column(nullable = false)
    var orderStatus: OrderStatus = PENDING

    protected constructor() : this(0L, mutableListOf())

    init {
        mapOrder(orderLineItemEntities)
    }

    private fun mapOrder(orderLineItemEntities: List<OrderLineItemEntity>) {

        for (orderItem in orderLineItemEntities) {
            orderItem.mapOrder(this)
        }
    }

    fun completeToSuccess() {
        this.orderStatus = SUCCESS
    }

    fun completeToFail() {
        this.orderStatus = FAIL
    }

    val isSuccess
        get() = orderStatus == SUCCESS

    val isFail
        get() = orderStatus == FAIL

    companion object
}