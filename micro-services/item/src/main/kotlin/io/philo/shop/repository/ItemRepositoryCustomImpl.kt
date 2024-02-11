package io.philo.shop.repository

import io.philo.shop.domain.entity.ItemEntity
import io.philo.shop.messagequeue.producer.ItemEventPublisher
import io.philo.shop.messagequeue.producer.toEvent

@Deprecated("순환 의존성으로 인해 적용 보류")
class ItemRepositoryCustomImpl(
    private val itemRepository: ItemRepository,
    private val eventPublisher: ItemEventPublisher
): ItemRepositoryCustom {
    override fun saveAndPublish(item: ItemEntity) {

        itemRepository.save(item)
        val event = item.toEvent()
        eventPublisher.publishEvent(event)
    }
}
