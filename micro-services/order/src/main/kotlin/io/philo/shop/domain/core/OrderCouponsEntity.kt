package io.philo.shop.domain.core

import io.philo.shop.constant.ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "order_coupons")
class OrderCouponsEntity(

    @Column
    val userCouponId1: Long? = null,

    @Column
    val userCouponId2: Long? = null,

    @OneToOne(mappedBy = "coupons")
    var orderLineItemEntity: OrderLineItemEntity,

) : BaseEntity() {

    protected constructor() : this(null, null, OrderLineItemEntity.empty)


    companion object {

        /**
         * ids가 없는 경우(null)도 null-safe 처리한다.
         */
        fun of(orderLineEntity: OrderLineItemEntity, userCouponIds: List<Long>?): OrderCouponsEntity? {

            if (userCouponIds.isNullOrEmpty()) return null
            require(userCouponIds.size in 1..2) { ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE }

            val couponId1 = userCouponIds.getOrNull(0)
            val couponId2 = userCouponIds.getOrNull(1)

            return OrderCouponsEntity(couponId1, couponId2, orderLineEntity)
        }
    }
}
