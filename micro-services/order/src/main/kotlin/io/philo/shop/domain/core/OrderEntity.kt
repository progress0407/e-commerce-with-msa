package io.philo.shop.domain.core

import io.philo.shop.constant.OrderStatus
import io.philo.shop.constant.OrderStatus.*
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.FetchType.LAZY
import lombok.ToString

@Entity
@Table(name = "orders")
class OrderEntity (

    val requesterId: Long,

    @OneToMany(mappedBy = "orderEntity", cascade = [ALL], orphanRemoval = true, fetch = LAZY)
    val orderLineItemEntities: MutableList<OrderLineItemEntity> = mutableListOf()

) : BaseEntity() {

    @Column(nullable = false)
    var totalOrderAmount: Int = 0

    @Enumerated(STRING)
    @Column(nullable = false)
    var orderStatus: OrderStatus = PENDING

    @OneToMany(mappedBy = "orderEntity", cascade = [ALL], orphanRemoval = true, fetch = LAZY)
    val orderHistories: MutableList<OrderHistoryEntity> = mutableListOf(OrderHistoryEntity(orderEntity = this))

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
        orderHistories.add(OrderHistoryEntity(this, SUCCESS))
    }

    fun completeToFail() {
        this.orderStatus = FAIL
        orderHistories.add(OrderHistoryEntity(this, FAIL))
    }

    val isSuccess
        get() = orderStatus == SUCCESS

    val isFail
        get() = orderStatus == FAIL

    override fun toString(): String {
        return "OrderEntity(requesterId=$requesterId, totalOrderAmount=$totalOrderAmount, orderStatus=$orderStatus, isSuccess=$isSuccess, isFail=$isFail)"
    }

    companion object {

        @JvmStatic
        fun empty() = OrderEntity()

/*
        @JvmStatic
        fun of(requesterId: Long, orderLineItemEntities: MutableList<OrderLineItemEntity>): OrderEntity {
            return OrderEntity(requesterId, orderLineItemEntities)
        }
*/
    }
}