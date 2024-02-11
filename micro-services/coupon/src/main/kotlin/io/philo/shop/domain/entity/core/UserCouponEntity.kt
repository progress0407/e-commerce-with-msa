package io.philo.shop.domain.entity.core

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
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

    constructor() : this(-1L, -1L)
}

