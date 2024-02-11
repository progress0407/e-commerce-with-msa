package io.philo.shop.domain.service

import io.philo.shop.domain.entity.ItemEntity
import io.philo.shop.error.InAppException
import io.philo.shop.item.dto.ItemInternalResponseDto
import io.philo.shop.presentation.dto.ItemResponse
import io.philo.shop.repository.ItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemService(private val itemRepository: ItemRepository) {

    @Transactional
    fun registerItem(
        name: String,
        size: String,
        price: Int,
        availableQuantity: Int
    ): Long {

        val itemEntity = ItemEntity(name, size, price, availableQuantity)
        itemRepository.save(itemEntity)

        return itemEntity.id!!
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

    @Transactional(readOnly = true)
    fun findItemsForInternal(itemIds: List<Long>): List<ItemInternalResponseDto> {

        val entities = itemRepository.findByIdIn(itemIds)
        val dtos = entities
            .map { item -> ItemInternalResponseDto(item.id!!, item.name, item.size, item.price) }
            .toList()
        return dtos
    }

    @Transactional
    fun decreaseItems(itemIdToDecreaseQuantity: Map<Long, Int>) {

        val itemIds = itemIdToDecreaseQuantity.keys
        val findItems = itemRepository.findByIdIn(itemIds) // problem !
        validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity, findItems)
    }

    /**
     * 주문 전에 상품 가격이 맞는지, 현재 재고가 충분하지를 검증
     */
    @Transactional(readOnly = true)
    fun checkItemBeforeOrder(itemId: Long, itemAmount: Int, itemQuantity: Int): Boolean {

        val currentItem = itemRepository.findByIdOrNull(itemId)

        if (currentItem == null)
            return false
        else if (currentItem.price != itemAmount)
            return false
        else if (currentItem.stockQuantity - itemQuantity < 0) // 재고 수량이 0 이하로 내려갈 수 없다
            return false

        throw InAppException(INTERNAL_SERVER_ERROR, "데이터 형태가 올바르지 않습니다.")
    }

    private fun validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity: Map<Long, Int>, findItemEntities: List<ItemEntity>) {
        for (item in findItemEntities) {
            val decreaseQuantity = itemIdToDecreaseQuantity[item.id]!!
            item.decreaseStockQuantity(decreaseQuantity)
        }
    }
}