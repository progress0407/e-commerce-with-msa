package io.philo.shop.domain.entity

import jakarta.persistence.Entity

@Entity
class RatioDiscountCoupon(val discountPercent: Int) : Coupon() {

    protected constructor() : this(discountPercent = -1)

    init {
        if (discountPercent < 0 || discountPercent > 100) {
            throw RuntimeException("할인율은 0에서 100 (%) 사이여야 합니다.")
        }
    }

    override fun order(): Int = 2

    override fun discount(itemAmount: Int): Int {
        return itemAmount * (discountPercent / 100)
    }
}