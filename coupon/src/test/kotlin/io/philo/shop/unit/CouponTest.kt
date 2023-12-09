package io.philo.shop.unit

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.shop.domain.entity.FixedDiscountCoupon
import io.philo.shop.domain.entity.RatioDiscountCoupon
import io.philo.shop.domain.service.CouponDiscountCalculator.Companion.calculateDiscountAmount

class CouponTest : StringSpec({


    "할인된 금액을 계산한다" {

        // given
        val coupon_1 = RatioDiscountCoupon(discountPercent = 10)
        val coupon_2 = FixedDiscountCoupon(discountAmount = 1_000)

        // when
        // (10,000 - 1,000) * 90% = 8,100
        val discountedAmount = calculateDiscountAmount(10_000, coupon_1, coupon_2)

        // then
        discountedAmount shouldBe 8100
    }
})
