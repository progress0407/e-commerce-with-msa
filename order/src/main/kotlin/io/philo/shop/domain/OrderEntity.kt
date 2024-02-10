package io.philo.shop.domain

import io.philo.shop.constant.OrderStatus
import jakarta.persistence.*
import lombok.ToString

@Entity
@Table(name = "orders")
@ToString(exclude = ["orderItems"])
class OrderEntity protected constructor(orderLineItemEntities: MutableList<OrderLineItemEntity>) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null

    @OneToMany(mappedBy = "orderEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderLineItemEntities: MutableList<OrderLineItemEntity> = mutableListOf()

    @Column(nullable = false)
    var totalOrderAmount: Int = 0

    @Enumerated
    @Column(nullable = false)
    var orderStatus: OrderStatus = OrderStatus.PENDING

    @Column(nullable = false)
    var itemValidated: Boolean = false

    @Column(nullable = false)
    var couponValidated: Boolean = false

    @Column(nullable = false)
    var paymentValidated: Boolean = false

    protected constructor() : this(mutableListOf())

    init {
        this.orderLineItemEntities = orderLineItemEntities
        mapOrder(orderLineItemEntities)
        totalOrderAmount = calculateTotalOrderAmount(orderLineItemEntities)
    }

    private fun calculateTotalOrderAmount(orderLineItemEntities: List<OrderLineItemEntity>) =
        orderLineItemEntities
            .stream()
            .mapToInt { obj: OrderLineItemEntity -> obj.orderItemAmount() }
            .sum()

    private fun mapOrder(orderLineItemEntities: List<OrderLineItemEntity>) {

        for (orderItem in orderLineItemEntities) {
            orderItem.mapOrder(this)
        }
    }

    companion object {

        @JvmStatic
        fun createOrder(orderLineItemEntities: MutableList<OrderLineItemEntity>): OrderEntity = OrderEntity(orderLineItemEntities)
    }
}