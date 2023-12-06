package io.philo.shop.application

import io.philo.shop.domain.Order
import io.philo.shop.domain.OrderItem
import io.philo.shop.dto.web.CreateOrderRequest
import io.philo.shop.dto.web.OrderLineRequest
import io.philo.shop.item.ItemRestClientFacade
import io.philo.shop.item.dto.ItemInternalResponse
import io.philo.shop.message.RabbitMqEventPublisher
import io.philo.shop.repository.OrderRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class OrderService(
    private val orderRepository: OrderRepository,
    private val itemClient: ItemRestClientFacade,
    private val rabbitMqEventPublisher: RabbitMqEventPublisher
) {

    @Transactional
    fun order(orderRequest: CreateOrderRequest): Long {
        val orderLineRequests = orderRequest.orderLineRequests
        val itemIds = extractItemIds(orderLineRequests)
        val itemResponses = itemClient.requestItems(itemIds)
        val orderItems = createOrderLines(itemResponses, orderLineRequests)
        val order = Order.createOrder(orderItems)
        val savedOrder = orderRepository.save(order)
        rabbitMqEventPublisher.sendMessage("hello")
        return savedOrder.id!!
    }

    private fun createOrderLines(
        itemResponses: List<ItemInternalResponse>,
        orderLineRequests: List<OrderLineRequest>
    ): MutableList<OrderItem> {
        return orderLineRequests
            .map { request: OrderLineRequest -> createOrderLine(itemResponses, request) }
            .toMutableList()
    }

    private fun createOrderLine(itemResponses: List<ItemInternalResponse>, orderLineRequest: OrderLineRequest): OrderItem {
        val itemResponse = findItemDtoFromOrderLineRequest(itemResponses, orderLineRequest)
        return OrderItem(
            itemResponse.id,
            itemResponse.name,
            itemResponse.size,
            itemResponse.amount,
            orderLineRequest.quantity
        )
    }

    companion object {
        private fun extractItemIds(orderLineRequests: List<OrderLineRequest>): List<Long> {
            return orderLineRequests
                .map(OrderLineRequest::itemId)
                .toList()
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
}