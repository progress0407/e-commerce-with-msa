package io.philo.shop.item.domain.service

import io.philo.shop.item.domain.entity.Item
import io.philo.shop.item.presentation.dto.ItemResponse
import io.philo.shop.item.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemService(private val itemRepository: ItemRepository) {

    @Transactional
    fun registerItem(name: String,
                     size: String,
                     price: Int,
                     availableQuantity: Int): Long {

        val item = Item(name, size, price, availableQuantity)
        itemRepository.save(item)

        return item.id!!
    }

    @Transactional(readOnly = true)
    fun findItems(itemIds: List<Long>?): List<ItemResponse> {

        if (itemIds.isNullOrEmpty()) {
            val entities = itemRepository.findAll()
            val dtos = entities
                .map { item -> ItemResponse(item) }
                .toList()
            return dtos
        } else {
            val entities = itemRepository.findByIdIn(itemIds)
            val dtos = entities
                .map { item -> ItemResponse(item) }
                .toList()
            return dtos
        }
    }

    @Transactional
    fun decreaseItems(itemIdToDecreaseQuantity: Map<Long, Int>) {
        val itemIds = itemIdToDecreaseQuantity.keys
        val findItems = itemRepository.findByIdIn(itemIds) // problem !
        validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity, findItems)
    }

    private fun validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity: Map<Long, Int>, findItems: List<Item>) {
        for (item in findItems) {
            val decreaseQuantity = itemIdToDecreaseQuantity[item.id]!!
            item.decreaseStockQuantity(decreaseQuantity)
        }
    }
}