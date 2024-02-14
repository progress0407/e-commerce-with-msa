package io.philo.shop.scheduler

import io.philo.shop.application.OrderEventService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * OutBox에 저장한 이벤트를 브로커에 적재하는 역할
 */
@Component
class OrderEventLoader(
    private val orderEventService: OrderEventService
) {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelay = 1_000)
    fun loadEventToBroker() {
        orderEventService.loadEventToBroker()
    }

    @Scheduled(fixedDelay = 1_000)
    fun loadCompensatingEventToBroker() {
        orderEventService.loadCompensatingEventToBroker()
    }
}
