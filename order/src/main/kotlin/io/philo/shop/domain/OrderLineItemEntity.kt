package io.philo.shop.domain

import jakarta.persistence.*
import lombok.ToString

@Entity
@ToString(exclude = ["order"])
class OrderLineItemEntity(
    @field:Column(nullable = false, length = 100) val itemId: Long,

    @field:Column(nullable = false) val orderItemActualPrice: Int, // 실제 주문 가격

    @field:Column(nullable = false) val orderedQuantity: Int, // 주문 수량
) {

    constructor() : this(0L, 0, 0)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    val id: Long? = null

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var orderEntity: OrderEntity? = null

    fun mapOrder(orderEntity: OrderEntity?) {
        this.orderEntity = orderEntity
    }

    fun orderItemAmount(): Int {
        return orderItemActualPrice * orderedQuantity
    }
}