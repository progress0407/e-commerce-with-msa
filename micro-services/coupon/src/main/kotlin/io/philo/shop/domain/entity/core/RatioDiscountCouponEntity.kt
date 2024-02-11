package io.philo.shop.domain.entity.core

import jakarta.persistence.Entity

/**
 * 비율 할인
 */
@Entity
class RatioDiscountCouponEntity(name: String, val discountPercent: Int) : CouponEntity(name = name) {

    protected constructor() : this(name = "", discountPercent = 50)

    init {
        require(discountPercent in 5..95) {
            "쿠폰의 할인율은 5에서 95 (%) 사이여야 합니다."
        }
    }

    override fun order() = 2

    /**
     * 할인된 금액을 계산
     *
     * 상품가 10,000에 10%일 경우
     *
     * 10,000 * 90% = 9,000
     */
    override fun discount(itemAmount: Int): Int {
        return (itemAmount * ((100 - discountPercent).toDouble() / 100)).toInt()
    }
}