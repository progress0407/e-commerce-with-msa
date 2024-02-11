package io.philo.shop.scheduler

import io.philo.shop.message.OrderEventPublisher
import io.philo.shop.repository.OrderOutBoxTableRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * OutBox에 저장한 이벤트를 브로커에 적재하는 역할
 */
@Component
class EventLoadScheduler(
    private val outBoxTableRepository: OrderOutBoxTableRepository,
    private val orderEventPublisher: OrderEventPublisher,
) {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelay = 1_000)
    fun loadEventToBroker() {
        val events = outBoxTableRepository.findAllByLoadedIsFalse()

        if(events.isNullOrEmpty())
            return

        log.info { "브로커에 적재할 이벤트가 존재합니다." }

        for (event in events) {
            orderEventPublisher.publishEvent(event)
        }
    }
}