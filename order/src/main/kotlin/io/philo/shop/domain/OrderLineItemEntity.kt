package io.philo.shop.domain

import jakarta.persistence.*
import lombok.ToString

@Entity
@ToString(exclude = ["order"])
class OrderLineItemEntity(
    @field:Column(nullable = false, length = 100)
    val itemId: Long,

    @field:Column(nullable = false)
    val itemRawAmount: Int, // 주문 당시의 상품 가격

    @field:Column(nullable = false)
    val itemDiscountedAmount: Int, // 주문 당시의 할인된 상품 가격

    @field:Column(nullable = false)
    val orderedQuantity: Int, // 주문 수량
) {

    protected constructor() : this(0L, 0, 0, 0)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    val id: Long? = null

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var orderEntity: OrderEntity

    @JoinColumn(name = "coupon_id", nullable = true)
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var coupons: OrderCouponsEntity? = null

    fun mapOrder(orderEntity: OrderEntity) {
        this.orderEntity = orderEntity
    }

    fun orderItemAmount(): Int {
        return itemDiscountedAmount * orderedQuantity
    }

    val useCoupon: Boolean
        get() = this.coupons != null

    /**
     * null인 경우도 고려되었다. (null-safe)
     */
    fun initUserCoupon(userCouponIds: List<Long>?) {

        this.coupons = OrderCouponsEntity.of(this, userCouponIds)
    }

    companion object {
        val empty: OrderLineItemEntity
            get() = OrderLineItemEntity()
    }
}