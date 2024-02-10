package io.philo.shop.domain

import io.philo.shop.constant.ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*

/**
 * 주문 생성 후 이벤트 발행을 하기 위한 이벤트 저장 테이블
 */
@Entity
class OrderLineOutBox(

    @Column(nullable = false)
    val orderId: Long,

    @Column(nullable = false)
    val itemId: Long,

    @Column(nullable = false)
    val itemAmount: Int,

    @Column(nullable = false)
    val itemDiscountedAmount: Int,

    @Column(nullable = false)
    val itemQuantity: Int,

    ) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null

    @Column(nullable = false)
    final var userCouponId1: Long? = null
        private set

    @Column(nullable = false)
    final var userCouponId2: Long? = null
        private set

    @Column(nullable = false)
    private var loaded: Boolean = false // 발송 여부

    protected constructor () : this(0L, 0L, 0, 0, 0)

    fun initUserCouponIds(userCouponIds: List<Long>?) {

        if (userCouponIds.isNullOrEmpty()) return
        require(userCouponIds.size in 1..2) { ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE }

        this.userCouponId1 = userCouponIds[0]
        if (userCouponIds.size == 2)
            this.userCouponId2 = userCouponIds[1]
    }

    fun load() {
        this.loaded = true
    }
}