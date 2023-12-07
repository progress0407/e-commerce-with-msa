package io.philo.shop.application

import io.philo.shop.domain.Order
import io.philo.shop.domain.OrderItem
import io.philo.shop.dto.web.OrderLineRequest
import io.philo.shop.item.ItemRestClientFacade
import io.philo.shop.item.dto.ItemInternalResponse
import io.philo.shop.message.OrderEventPublisher
import io.philo.shop.repository.OrderRepository
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class OrderService(
    private val orderRepository: OrderRepository,
    private val itemClient: ItemRestClientFacade,
    private val orderEventPublisher: OrderEventPublisher
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun order(orderLineRequests: List<OrderLineRequest>): Long {

        val itemIds = extractItemIds(orderLineRequests)
        val itemResponses = itemClient.requestItems(itemIds)
        val orderItems = createOrderLines(itemResponses, orderLineRequests)
        val order = Order.createOrder(orderItems)
        orderRepository.save(order)
        orderEventPublisher.publishEvent(order)

        return order.id!!
    }

    private fun extractItemIds(orderLineRequests: List<OrderLineRequest>): List<Long> {
        return orderLineRequests
            .map(OrderLineRequest::itemId)
            .toList()
    }

    private fun createOrderLines(
        itemResponses: List<ItemInternalResponse>,
        orderLineRequests: List<OrderLineRequest>
    ): MutableList<OrderItem> {
        return orderLineRequests
            .map { request: OrderLineRequest -> createOrderLine(itemResponses, request) }
            .toMutableList()
    }

    private fun createOrderLine(
        itemResponses: List<ItemInternalResponse>,
        orderLineRequest: OrderLineRequest
    ): OrderItem {
        val itemResponse = findItemDtoFromOrderLineRequest(itemResponses, orderLineRequest)
        return OrderItem(
            itemResponse.id,
            itemResponse.name,
            itemResponse.size,
            itemResponse.amount,
            orderLineRequest.quantity
        )
    }

    private fun findItemDtoFromOrderLineRequest(
        itemResponses: List<ItemInternalResponse>,
        orderLineRequest: OrderLineRequest
    ): ItemInternalResponse {
        return itemResponses.stream()
            .filter { it: ItemInternalResponse -> it.id == orderLineRequest.itemId }
            .findAny()
            .orElseThrow { IllegalArgumentException("주문 항목 요청에 해당하는 상품이 없습니다.") }
    }
}