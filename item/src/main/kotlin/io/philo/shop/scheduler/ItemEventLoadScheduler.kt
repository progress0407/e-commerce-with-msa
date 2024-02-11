package io.philo.shop.scheduler

import io.philo.shop.domain.outbox.ItemOutBox
import io.philo.shop.item.ItemVerificationEvent
import io.philo.shop.queue.producer.ItemEventPublisher
import io.philo.shop.repository.ItemOutBoxRepository
import io.philo.shop.repository.ItemRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ItemEventLoadScheduler(
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
        val events:List<ItemVerificationEvent> = outboxes.convertToEvents()
        val outboxMap: Map<Long, ItemOutBox> = outboxes.associateBy { it.orderId }

        for (event in events) {
            itemEventPublisher.publishEvent(event)
            // todo!
            // kafka의 경우 event에 적재됨을 확인하면, (acks=1 이상)
            // 이후에 Load 상태로 바꾸게 변경하자

            changeOutBoxStatusToLoad(outboxMap, event)
        }
    }

    private fun List<ItemOutBox>.extractIds() =
        this.map { it.orderId }.toList()

    private fun List<ItemOutBox>.convertToEvents(): List<ItemVerificationEvent> =
        this.map { ItemVerificationEvent(it.orderId, it.verification) }

    private fun changeOutBoxStatusToLoad(outboxMap: Map<Long, ItemOutBox>, event: ItemVerificationEvent) {

        val matchedOutBox = outboxMap[event.orderId]!!
        matchedOutBox.load()
        itemOutBoxRepository.save(matchedOutBox)
    }
}
