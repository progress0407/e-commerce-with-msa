package io.philo.shop.coupon.domain.entity

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
class UserCoupon(
    @field:Column(nullable = false, unique = true)
    val userId: Long,

    @field:Column
    val couponId: Long,

    @field:Column
    var isUse: Boolean = false// 사용 여부
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null


    constructor() : this(-1L, -1L)
}

