package msa.with.ddd.item.application

import msa.with.ddd.item.domain.Item
import msa.with.ddd.item.repository.ItemRepository
import msa.with.ddd.item.dto.item.ItemCreateRequest
import msa.with.ddd.item.dto.item.ItemResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemService(private val itemRepository: ItemRepository) {

    @Transactional
    fun registerItem(request: ItemCreateRequest): Long {
        val item = createItem(request)
        val savedItem = itemRepository.save(item)
        return savedItem.id!!
    }

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

    private fun createItem(request: ItemCreateRequest) =
        Item(request.name, request.size, request.price, request.availableQuantity)

    private fun validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity: Map<Long, Int>, findItems: List<Item>) {
        for (item in findItems) {
            val decreaseQuantity = itemIdToDecreaseQuantity[item.id]!!
            item.decreaseQuantity(decreaseQuantity)
        }
    }
}