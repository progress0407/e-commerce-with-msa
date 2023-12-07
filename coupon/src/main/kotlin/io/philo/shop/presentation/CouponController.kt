package io.philo.shop.presentation

import io.philo.shop.domain.service.CouponService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/coupon")
@RestController
class CouponController(private val couponService: CouponService) {

    /**
     * Coupon 생성
     *
     * 시스템적으로 생성할 것인지 아니면,
     *
     * 사람이 생성하게 둘 것인지
     */
    @PostMapping
    fun createCoupon(): Unit {

        return couponService.createCoupon()
    }

    @GetMapping("/coupon-applied-amount")
    fun calculateAmount(): Int {

//        todo return couponService.calculateAmount()
        return -1
    }
}