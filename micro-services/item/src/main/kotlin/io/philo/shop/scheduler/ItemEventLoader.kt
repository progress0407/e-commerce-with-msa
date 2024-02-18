package io.philo.shop.scheduler

import io.philo.shop.domain.service.ItemEventService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ItemEventLoader(private val itemEventService: ItemEventService) {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelay = 1_000)
    fun loadEventToBroker() {
        itemEventService.loadEventToBroker()
    }

    @Scheduled(fixedDelay = 1_000)
    fun loadCompensatingEventToBroker() {
        itemEventService.loadCompensatingEventToBroker()
    }
}
