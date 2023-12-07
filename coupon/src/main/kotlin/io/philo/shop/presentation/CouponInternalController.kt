package io.philo.shop.presentation

import io.philo.shop.domain.service.CouponService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/coupon/internal")
@RestController
class CouponInternalController(private val couponService: CouponService) {

    @GetMapping("/coupon-applied-amount")
    fun calculateAmount(): Int {

        return couponService.calculateAmountForInternal()
    }
}