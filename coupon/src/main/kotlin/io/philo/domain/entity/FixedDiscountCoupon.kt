package io.philo.domain.entity

class FixedDiscountCoupon(val discountAmount: Int) : Coupon() {

    override fun order(): Int = 1

    override fun discount(itemAmount: Int): Int {
        validateDiscount(itemAmount)
        return itemAmount - discountAmount
    }

    private fun validateDiscount(itemAmount: Int) {
        if (itemAmount / 2 < discountAmount) {
            throw RuntimeException("할인액이 너무 많습니다. (쿠폰 하나로 상품가의 50% 이상의 할인은 불가합니다.)")
        }
    }
}