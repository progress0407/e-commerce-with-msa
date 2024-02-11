package io.philo.shop.domain.service

import io.philo.shop.domain.entity.ItemEntity
import io.philo.shop.error.InAppException
import io.philo.shop.item.dto.ItemInternalResponseDto
import io.philo.shop.messagequeue.producer.ItemEventPublisher
import io.philo.shop.messagequeue.producer.toEvent
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.presentation.dto.ItemResponse
import io.philo.shop.repository.ItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
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
    fun decreaseItems(itemIdToDecreaseQuantity: Map<Long, Int>) {

        val itemIds = itemIdToDecreaseQuantity.keys
        val findItems = itemRepository.findAllByIdIn(itemIds) // problem !
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

        // 재고 수량이 0 이하로 내려갈 수 없다
        if (currentItem.price == itemAmount && currentItem.stockQuantity - itemQuantity >= 0)
            return true

        // 그 이외의 경우는 허용하지 않습니다
        throw InAppException(INTERNAL_SERVER_ERROR, "데이터 형태가 올바르지 않습니다.")
    }

    /**
     * 주문 전에 상품 가격이 맞는지, 현재 재고가 충분하지를 검증
     */
    @Transactional(readOnly = true)
    fun checkItemBeforeOrder(events: List<OrderLineCreatedEvent>): Boolean {

        val itemIds = events.map { it.itemId }.toList()
        val items = itemRepository.findAllByIdIn(itemIds)

        if (events.size != items.size) // DB에 존재하지 않는 상품 존재
            return false

        val eventMap = events.associateBy { it.itemId }
        for (item in items) {
            val event = eventMap[item.id!!]

            // 존재하지 않는 이벤트일 경우
            if (event == null)
                return false

            // 재고 수량이 0 이하로 내려갈 수 없다
            if (event.itemAmount == item.price && item.stockQuantity - event.itemQuantity >= 0)
                continue

            // 그 이외의 경우는 허용하지 않는다
            return false
        }

        return true
    }

    private fun validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity: Map<Long, Int>, findItemEntities: List<ItemEntity>) {
        for (item in findItemEntities) {
            val decreaseQuantity = itemIdToDecreaseQuantity[item.id]!!
            item.decreaseStockQuantity(decreaseQuantity)
        }
    }
}