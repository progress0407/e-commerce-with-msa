package io.philo.shop.domain.service

import io.philo.shop.domain.entity.Coupon

class CouponDiscountCalculator {

    companion object {

        /**
         * 상품 할인액을 계산한다
         *
         * 고정 할인을 비율 할인보다 먼저 계산한다
         */
        fun calculateDiscountAmount(itemAmount: Int, vararg coupons: Coupon): Int {

            return calculateDiscountAmount(itemAmount, coupons.toList())
        }

        /**
         * @see calculateDiscountAmount
         */
        fun calculateDiscountAmount(itemAmount: Int, coupons: List<Coupon>): Int {

            val discountedAmount = coupons.toList()
                .sortedBy { it.order }
                .fold(itemAmount) { nextAmount, coupon -> coupon.discount(nextAmount) }

            require(discountedAmount >= 0) {
                "상품 가격은 음수가 될 수 없습니다 (${discountedAmount})"
            }

            return discountedAmount
        }

    }
}