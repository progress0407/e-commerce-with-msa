package io.philo.shop.unit

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.shop.domain.entity.FixedDiscountCoupon
import io.philo.shop.domain.entity.RatioDiscountCoupon

class CouponTest : StringSpec({


    "할인된 금액을 계산한다" {

        // given
        val coupon_1 = FixedDiscountCoupon(discountAmount = 1_000)
        val coupon_2 = RatioDiscountCoupon(discountPercent = 10)

        // when
        // (10,000 - 1,000) * 90% = 8,100
        val discountedAmount = listOf(coupon_1, coupon_2)
            .fold(10_000) { nextAmount, coupon -> coupon.discount(nextAmount) }

        // then
        discountedAmount shouldBe 8100
    }
})
