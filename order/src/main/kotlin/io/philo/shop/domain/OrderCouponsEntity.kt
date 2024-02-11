package io.philo.shop.domain

import io.philo.shop.constant.ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE
import io.philo.shop.entity.BaseEntity
import io.philo.shop.error.InAppException
import jakarta.persistence.*
import org.springframework.http.HttpStatus.BAD_REQUEST

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null

    protected constructor() : this(null, null, OrderLineItemEntity.empty)


    companion object {

        /**
         * ids가 없는 경우(null)도 null-safe 처리한다.
         */
        fun of(orderLineEntity: OrderLineItemEntity, userCouponIds: List<Long>?): OrderCouponsEntity? {

            if (userCouponIds.isNullOrEmpty()) return null
            require(userCouponIds.size in 1..2) { ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE }

            val couponId1 = userCouponIds[0]
            val couponId2 = userCouponIds[1]

            return when (userCouponIds.size) {
                1 -> OrderCouponsEntity(couponId1, null, orderLineEntity)
                2 -> OrderCouponsEntity(couponId1, couponId2, orderLineEntity)
                else -> throw InAppException(BAD_REQUEST, ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE)
            }
        }
    }
}
