package io.philo.shop.domain.core

import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import lombok.ToString

@Entity
@Table(name = "order_line_item")
class OrderLineItemEntity(

    @field:Column(nullable = false, length = 100)
    val itemId: Long,

    @field:Column(nullable = false)
    val itemRawAmount: Int, // 주문 당시의 상품 가격

    @field:Column(nullable = false)
    val itemDiscountedAmount: Int, // 주문 당시의 할인된 상품 가격

    @field:Column(nullable = false)
    val orderedQuantity: Int, // 주문 수량

): BaseEntity() {

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var orderEntity: OrderEntity

    @JoinColumn(name = "coupon_id", nullable = true)
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var coupons: OrderCouponsEntity? = null

    protected constructor() : this(0L, 0, 0, 0)

    fun mapOrder(orderEntity: OrderEntity) {
        this.orderEntity = orderEntity
    }

    val useCoupon: Boolean
        get() = this.coupons != null

    /**
     * null인 경우도 고려되었다. (null-safe)
     */
    fun initUserCoupon(userCouponIds: List<Long>?) {
        this.coupons = OrderCouponsEntity.of(this, userCouponIds)
    }

    override fun toString(): String {
        return "OrderLineItemEntity(itemId=$itemId, itemRawAmount=$itemRawAmount, itemDiscountedAmount=$itemDiscountedAmount, orderedQuantity=$orderedQuantity, id=$id, useCoupon=$useCoupon)"
    }

    companion object {
        val empty: OrderLineItemEntity
            get() = OrderLineItemEntity()
    }
}