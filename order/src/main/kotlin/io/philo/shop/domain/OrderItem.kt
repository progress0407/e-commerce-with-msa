package io.philo.shop.domain

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = ["order"])
class OrderItem(
    @field:Column(nullable = false, length = 100) val itemId: Long,

    @field:Column(nullable = false) val itemName: String,

    @field:Column(nullable = false) val size: String,

    @field:Column(nullable = false) val orderItemPrice: Int,

    @field:Column(nullable = false) val orderItemQuantity: Int
) {

    constructor() : this(0L, "", "", 0, 0)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private val id: Long? = null

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private var order: Order? = null

    fun mapOrder(order: Order?) {
        this.order = order
    }

    fun orderItemAmount(): Int {
        return orderItemPrice * orderItemQuantity
    }
}