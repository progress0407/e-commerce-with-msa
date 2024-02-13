package io.philo.shop.scheduler

import io.philo.shop.common.OrderCreatedVerifiedEvent
import io.philo.shop.domain.outbox.ItemOutBox
import io.philo.shop.messagequeue.producer.ItemEventPublisher
import io.philo.shop.repository.ItemOutBoxRepository
import io.philo.shop.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ItemEventLoader(
    private val itemOutBoxRepository: ItemOutBoxRepository,
    private val itemRepository: ItemRepository,
    private val itemEventPublisher: ItemEventPublisher
) {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelay = 1_000)
    fun loadEventToBroker() {

        val outboxes = itemOutBoxRepository.findAllByLoadedIsFalse()

        if (outboxes.isNullOrEmpty())
            return

        log.info { "브로커에 적재할 이벤트가 존재합니다." }

        val itemIds = outboxes.extractIds()
        val events:List<OrderCreatedVerifiedEvent> = outboxes.convertToEvents()
        val outboxMap: Map<Long, ItemOutBox> = outboxes.associateBy { it.traceId }

        for (event in events) {
            itemEventPublisher.publishEvent(event)
            changeOutBoxStatusToLoad(outboxMap, event)
        }
    }

    private fun List<ItemOutBox>.extractIds() =
        this.map { it.traceId }.toList()

    private fun List<ItemOutBox>.convertToEvents(): List<OrderCreatedVerifiedEvent> =
        this.map { OrderCreatedVerifiedEvent(it.traceId, it.verification) }

    private fun changeOutBoxStatusToLoad(outboxMap: Map<Long, ItemOutBox>, event: OrderCreatedVerifiedEvent) {

        val matchedOutBox = outboxMap[event.orderId]!!
        matchedOutBox.load()
        itemOutBoxRepository.save(matchedOutBox)
    }
}
