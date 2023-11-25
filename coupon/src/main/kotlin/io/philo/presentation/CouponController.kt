package io.philo.presentation

import io.philo.domain.service.CouponService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/coupon")
@RestController
class CouponController(private val couponService: CouponService) {

    @GetMapping("/coupon-applied-amount")
    fun calculateAmount(): Int {

//        todo return couponService.calculateAmount()
        return -1
    }
}