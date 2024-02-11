package io.philo.shop.domain.service

import io.philo.shop.domain.entity.core.CouponEntity

class CouponDiscountCalculator {

    companion object {

        /**
         * 상품 할인액을 계산한다
         *
         * 고정 할인을 비율 할인보다 먼저 계산한다
         */
        fun calculateDiscountAmount(itemAmount: Int, vararg couponEntities: CouponEntity): Int {

            return calculateDiscountAmount(itemAmount, couponEntities.toList())
        }

        /**
         * @see calculateDiscountAmount
         */
        fun calculateDiscountAmount(itemAmount: Int, couponEntities: List<CouponEntity>): Int {

            val discountedAmount = couponEntities.toList()
                .sortedBy { it.order }
                .fold(itemAmount) { nextAmount, coupon -> coupon.discount(nextAmount) }

            require(discountedAmount >= 0) {
                "상품 가격은 음수가 될 수 없습니다 (${discountedAmount})"
            }

            return discountedAmount
        }

    }
}