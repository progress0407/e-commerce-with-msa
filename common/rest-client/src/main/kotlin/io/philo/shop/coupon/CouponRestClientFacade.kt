package io.philo.shop.coupon

import org.springframework.stereotype.Component

@Component
class CouponRestClientFacade(private val couponFeignClient: CouponFeignClient) {

    fun requestItemCostsByIds(ids: List<Long>): Map<Long, Int> {
        return couponFeignClient.requestItemCostsByIds(ids)
    }
}