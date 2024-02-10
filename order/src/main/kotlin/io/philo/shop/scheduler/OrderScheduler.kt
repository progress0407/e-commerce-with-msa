package io.philo.shop.scheduler

import io.philo.shop.message.OrderEventPublisher
import io.philo.shop.repository.OrderOutBoxTableRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OrderScheduler(
    private val outBoxTableRepository: OrderOutBoxTableRepository,
    private val orderEventPublisher: OrderEventPublisher,
) {

    @Scheduled(fixedDelay = 2_000)
    fun loadEventToBroker() {
        val events = outBoxTableRepository.findAllByLoadedIsFalse()

        for (event in events) {
            orderEventPublisher.publishEvent(event)
        }
    }
}