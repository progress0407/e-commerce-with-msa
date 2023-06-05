package msa.with.ddd.order.domain

import msa.with.ddd.order.dto.event.OrderCreatedEvent
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = ["orderItems"])
open class Order protected constructor(orderItems: List<OrderItem>) : AbstractAggregateRoot<Order>() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    private var orderItems: List<OrderItem> = mutableListOf()

    @Column(nullable = false)
    open val totalOrderAmount: Int

    init {
        this.orderItems = orderItems
        mapOrder(orderItems)
        totalOrderAmount = calculateTotalOrderAmount(orderItems)
        registerEvent(msa.with.ddd.order.dto.event.OrderCreatedEvent(orderItems))
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
        fun createOrder(orderItems: List<OrderItem>): Order = Order(orderItems)
    }
}