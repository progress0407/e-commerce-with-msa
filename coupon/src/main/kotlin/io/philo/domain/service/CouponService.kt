package io.philo.domain.service

import io.philo.domain.entity.Coupon
import org.springframework.stereotype.Service

@Service
class CouponService {

    fun discountCoupon(userId: Long, itemId: Long, itemAmount: Int, couponIds: List<Long>): Int {

        val coupons: List<Coupon> =  // find Coupons and ordering coupons
            listOf<Coupon>()
                .sortedBy { it -> it.orer }
        val finalAmount = coupons.sumOf { it.discount(itemAmount) }

        if (finalAmount < 0) {
            throw IllegalArgumentException("상품 가격은 음수가 될 수 없습니다")
        }

        coupons[0].calculatedValue

        return finalAmount
    }
}