package io.philo.shop.domain.service

import io.philo.shop.domain.entity.ItemEntity
import io.philo.shop.domain.outbox.ItemOutboxEntity
import io.philo.shop.error.InAppException
import io.philo.shop.order.OrderChangedEvent
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.repository.ItemOutBoxRepository
import io.philo.shop.repository.ItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemEventService(private val itemOutBoxRepository: ItemOutBoxRepository, private val itemRepository: ItemRepository) {

    /**
     * 상품에 대한 유효성 검증을 하고 Outbox 데이터를 넣습니다.
     */
    @Transactional
    fun listenOrderCreatedEvent(event: OrderChangedEvent) {

        val orderLineEvents = event.orderLineCreatedEvents
        val itemVerification = checkItemBeforeOrder(orderLineEvents)
        if (itemVerification) {
            val itemMap = orderLineEvents.associateBy({ it.itemId }, { it.itemQuantity })
            decreaseItems(itemMap)
        }

        val outbox = ItemOutboxEntity(event.orderId, event.requesterId, itemVerification)
        itemOutBoxRepository.save(outbox)
    }

    @Transactional
    fun listenOrderFailedEvent(event: OrderChangedEvent) {

        val itemMap = event.orderLineCreatedEvents.associateBy({ it.itemId }, { it.itemQuantity })
        increaseItems(itemMap)
    }

    /**
     * 주문 전에 상품 가격이 맞는지, 현재 재고가 충분하지를 검증
     */
    fun checkItemBeforeOrder(events: List<OrderLineCreatedEvent>): Boolean {

        val itemIds = events.map { it.itemId }.toList()
        val items = itemRepository.findAllByIdIn(itemIds)

        if (events.size != items.size) // DB에 존재하지 않는 상품 존재
            return false

        val eventMap = events.associateBy { it.itemId }
        for (item in items) {
            val request = eventMap[item.id!!]

            // 존재하지 않는 이벤트일 경우
            if (request == null)
                return false

            // 상품 가격이 일치해야한다
            if (request.itemAmount != item.price)
                return false

            // 재고 수량이 0 이하로 내려갈 수 없다
            if(item.stockQuantity - request.itemQuantity < 0)
                return false
        }

        return true
    }

    /**
     * 수신 받은 이벤트 정보를 기준으로 상품들의 재고 수량을 감소시킨다
     *
     * @param itemMap <itemId, decrease quantity>
     */
    private fun decreaseItems(itemMap: Map<Long, Int>) {

        changeItemQuantity(itemMap) { item, quantity -> item.decreaseStockQuantity(quantity)}
    }

    /**
     * 다른 서비스의 실패로 인해 재고 수량을 다시 증가시킨다
     *
     * @see decreaseItems
     */
    private fun increaseItems(itemMap: Map<Long, Int>) {

        changeItemQuantity(itemMap) { item, quantity -> item.increaseStockQuantity(quantity)}
    }

    private fun changeItemQuantity(itemMap: Map<Long, Int>, changeQuantity: (ItemEntity, Int) -> Unit) {

        val itemIds = itemMap.keys
        val findItems = itemRepository.findAllByIdIn(itemIds)
        for (findItem in findItems) {
            val decreaseQuantity = itemMap[findItem.id!!]!!
            changeQuantity.invoke(findItem, decreaseQuantity)
        }
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
        throw InAppException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 형태가 올바르지 않습니다.")
    }
}
