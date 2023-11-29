package msa.with.ddd.item.domain.service

import msa.with.ddd.item.domain.entity.Item
import msa.with.ddd.item.presentation.dto.ItemResponse
import msa.with.ddd.item.repository.ItemRepository
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

        return if (itemIds.isNullOrEmpty()) {
            itemRepository.findAll()
                .map { item -> ItemResponse(item) }
                .toList()
        } else {
            itemRepository.findByIdIn(itemIds)
                .map { item -> ItemResponse(item) }
                .toList()
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