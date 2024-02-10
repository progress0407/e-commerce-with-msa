package io.philo.shop.coupon

import io.philo.shop.item.dto.ItemInternalResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "COUPON-SERVICE")
interface CouponFeignClient {

    @GetMapping("/coupon/internal")
    fun requestItems(@RequestParam("ids") ids: List<Long>): List<ItemInternalResponse>

    @GetMapping("/coupon/internal/discount-amounts")
    fun requestItemCostsByIds(ids: List<Long>): Map<Long, Int>
}