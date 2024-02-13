package io.philo.shop.domain.service

import io.philo.shop.domain.entity.ItemEntity
import io.philo.shop.item.dto.ItemInternalResponseDto
import io.philo.shop.messagequeue.producer.ItemEventPublisher
import io.philo.shop.messagequeue.producer.toEvent
import io.philo.shop.presentation.dto.ItemResponse
import io.philo.shop.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemService(
    private val itemRepository: ItemRepository,
    private val itemEventPublisher: ItemEventPublisher,
) {

    @Transactional
    fun registerItem(
        name: String,
        size: String,
        price: Int,
        availableQuantity: Int,
    ): Long {

        val entity = ItemEntity(name, size, price, availableQuantity)
        itemRepository.save(entity)
        itemEventPublisher.publishEvent(entity.toEvent())

        return entity.id!!
    }

    @Transactional(readOnly = true)
    fun findItems(itemIds: List<Long>?): List<ItemResponse> {

        return if (itemIds.isNullOrEmpty()) {
            val entities = itemRepository.findAll()
            val dtos = entities
                .map { item -> ItemResponse(item) }
                .toList()
            dtos
        } else {
            val entities = itemRepository.findAllByIdIn(itemIds)
            val dtos = entities
                .map { item -> ItemResponse(item) }
                .toList()
            dtos
        }
    }

    @Transactional(readOnly = true)
    fun findItemsForInternal(itemIds: List<Long>): List<ItemInternalResponseDto> {

        val entities = itemRepository.findAllByIdIn(itemIds)
        val dtos = entities
            .map { item -> ItemInternalResponseDto(item.id!!, item.name, item.size, item.price) }
            .toList()
        return dtos
    }

    @Transactional
    @Deprecated("미 사용")
    fun decreaseItemsDeprecated(itemIdToDecreaseQuantity: Map<Long, Int>) {

        val itemIds = itemIdToDecreaseQuantity.keys
        val findItems = itemRepository.findAllByIdIn(itemIds) // problem !
        validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity, findItems)
    }

    private fun validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity: Map<Long, Int>, findItemEntities: List<ItemEntity>) {
        for (item in findItemEntities) {
            val decreaseQuantity = itemIdToDecreaseQuantity[item.id]!!
            item.decreaseStockQuantity(decreaseQuantity)
        }
    }
}