package io.philo.shop.domain.service

import io.philo.shop.domain.entity.core.CouponEntity

class CouponDiscountCalculator {

    companion object {

        /**
         * 상품 할인액을 계산한다.
         * 이때, 비율할인보다 고정할인을 먼저 계산한다.
         * 이렇게 하는 이유는 쇼핑몰 입장에서 감액되는 금액을 적게 하기 위함이다.
         *
         * 수학적 직관적으로 해석하면
         * 고정할인은 상품금액과 무관하게 할인액이 항상 일정하지만
         * 비율할인은 상품금액에 따라 할인액이 비례한다.
         *
         * 예를 들어,
         * 가격이 10,000원인 상품에 대해
         * 20% 할인 후 2,000원 할인하면 6,000원이지만
         * 역순으로 하면 6,400원으로 더 크다. (이는 어떤 숫자가 오더라도 항상 참이다.)
         */
        fun calculateDiscountAmount(itemAmount: Int, vararg couponEntities: CouponEntity): Int {

            return calculateDiscountAmount(itemAmount, couponEntities.toList())
        }

        /**
         * couponEntities가 List 자료형으로 올 것을 고려하여 생성한 API
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