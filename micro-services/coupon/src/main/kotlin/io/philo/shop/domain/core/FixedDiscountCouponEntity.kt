package io.philo.shop.domain.core

import jakarta.persistence.Entity

/**
 * 고정 할인
 */
@Entity
class FixedDiscountCouponEntity(name: String, val discountAmount: Int) : CouponEntity(name = name) {

    protected constructor() : this(name = "", discountAmount = 0)

    init {
        require(discountAmount >= 0) {
            "할인액은 음수가 될 수 없습니다"
        }

    }

    override fun order() = 1

    override fun discount(itemAmount: Int): Int {
        validateDiscount(itemAmount)
        return itemAmount - discountAmount
    }

    private fun validateDiscount(itemAmount: Int) {
        check(itemAmount / 2 >= discountAmount) {
            "할인액이 너무 많습니다. (고정 금액 쿠폰 하나로 상품가의 50% 이상의 할인은 불가합니다.)"
        }
    }
}