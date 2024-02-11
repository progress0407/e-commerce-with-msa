package io.philo.shop.presentation

import io.philo.shop.domain.service.CouponService
import org.springframework.web.bind.annotation.*

@RequestMapping("/coupon/internal")
@RestController
class CouponInternalController(private val couponService: CouponService) {

//    @GetMapping("/coupon-applied-amount")
    @GetMapping("/discount-amounts")
    fun calculateAmount(@RequestHeader("loginUserId") userId: Long,
                        @RequestBody ids: List<Long>): Int {

        return couponService.calculateAmountForInternal(userId, ids)
    }

    // /coupon/internal/discount-amounts
}