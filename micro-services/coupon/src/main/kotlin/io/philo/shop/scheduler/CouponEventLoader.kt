package io.philo.shop.scheduler

import io.philo.shop.service.CouponEventService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CouponEventLoader(private val couponEventService: CouponEventService) {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelay = 1_000)
    fun loadEventToBroker() {
        couponEventService.loadEventToBroker()
    }

    @Scheduled(fixedDelay = 1_000)
    fun loadCompensatingEventToBroker() {
        couponEventService.loadCompensatingEventToBroker()
    }

}