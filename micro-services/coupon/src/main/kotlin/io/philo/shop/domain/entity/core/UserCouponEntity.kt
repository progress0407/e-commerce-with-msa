package io.philo.shop.domain.entity.core

import jakarta.persistence.*

@Entity
@Table(name = "user_coupon")
class UserCouponEntity(

    @field:Column(nullable = false)
    val userId: Long,

    @field:Column(nullable = false)
    val couponId: Long,

    @field:Column(nullable = false)
    var isUse: Boolean = false// 사용 여부
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null

    protected constructor() : this(-1L, -1L)
}

