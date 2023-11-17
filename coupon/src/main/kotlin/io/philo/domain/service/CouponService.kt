package io.philo.domain.service

import io.philo.domain.entity.Coupon
import org.springframework.stereotype.Service

@Service
class CouponService {

    fun discountCoupon(userId: Long, itemId: Long, itemAmount: Int, couponIds: List<Long>): Int {

        val coupons: List<Coupon> = listOf()
        val finalAmount = coupons.map { it.discount(itemAmount) }
            .toList()

        return finalAmount
    }
}