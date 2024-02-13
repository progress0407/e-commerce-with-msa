package io.philo.shop.domain.core

import jakarta.persistence.*

@Entity
@Table(name = "user_coupon")
class UserCouponEntity(

    @field:Column(nullable = false)
    val userId: Long,

    @field:Column(nullable = false)
    var isUse: Boolean = false // 사용 여부
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @JoinColumn(name = "coupon_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var coupon: CouponEntity

    protected constructor() : this(0L)

    fun useCoupon() {
        this.isUse = true
    }
}

