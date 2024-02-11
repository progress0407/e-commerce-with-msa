package io.philo.shop.domain

import io.philo.shop.constant.OrderStatus
import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import lombok.ToString

@Entity
@Table(name = "orders")
@ToString(exclude = ["orderItems"])
class OrderEntity (orderLineItemEntities: MutableList<OrderLineItemEntity>) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null

    @Column(nullable = false)
    var totalOrderAmount: Int = 0

    @Enumerated
    @Column(nullable = false)
    var orderStatus: OrderStatus = OrderStatus.PENDING

    @OneToMany(mappedBy = "orderEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = EAGER)
    var orderLineItemEntities: MutableList<OrderLineItemEntity> = mutableListOf()

    protected constructor() : this(mutableListOf())

    init {
        this.orderLineItemEntities = orderLineItemEntities
        mapOrder(orderLineItemEntities)
//        totalOrderAmount = calculateTotalOrderAmount(orderLineItemEntities)
    }

    private fun mapOrder(orderLineItemEntities: List<OrderLineItemEntity>) {

        for (orderItem in orderLineItemEntities) {
            orderItem.mapOrder(this)
        }
    }

/*
    private fun calculateTotalOrderAmount(orderLineItemEntities: List<OrderLineItemEntity>) =
        orderLineItemEntities
            .stream()
            .mapToInt { obj: OrderLineItemEntity -> obj.orderItemAmount() }
            .sum()
*/

    companion object
}