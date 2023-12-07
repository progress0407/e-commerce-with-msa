package io.philo.shop.domain

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = ["orderItems"])
class Order protected constructor(orderItems: MutableList<OrderItem>) {

    protected constructor() : this(mutableListOf())

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderItems: MutableList<OrderItem> = mutableListOf()

    @Column(nullable = false)
    val totalOrderAmount: Int

    init {
        this.orderItems = orderItems
        mapOrder(orderItems)
        totalOrderAmount = calculateTotalOrderAmount(orderItems)
    }

    private fun calculateTotalOrderAmount(orderItems: List<OrderItem>): Int {
        return orderItems.stream()
            .mapToInt { obj: OrderItem -> obj.orderItemAmount() }
            .sum()
    }

    private fun mapOrder(orderItems: List<OrderItem>) {
        for (orderItem in orderItems) {
            orderItem.mapOrder(this)
        }
    }

    companion object {
        @JvmStatic
        fun createOrder(orderItems: MutableList<OrderItem>): Order = Order(orderItems)
    }
}