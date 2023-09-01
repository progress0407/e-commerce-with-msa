package msa.with.ddd.order.application

import msa.with.ddd.order.domain.Order
import msa.with.ddd.order.domain.OrderItem
import msa.with.ddd.order.dto.web.CreateOrderRequest
import msa.with.ddd.order.dto.web.ItemResponse
import msa.with.ddd.order.dto.web.OrderLineRequest
import msa.with.ddd.order.repository.OrderRepository
import msa.with.ddd.order.rest.ItemFeignClient
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class OrderService(
    private val orderRepository: OrderRepository,
    private val itemHttpClient: ItemFeignClient
) {

    @Transactional
    fun order(orderRequest: CreateOrderRequest): Long {
        val orderLineRequests = orderRequest.orderLineRequests
        val itemIds = extractItemIds(orderLineRequests)
        val itemResponses = itemHttpClient.requestItems(itemIds).items
        val orderItems = createOrderLines(itemResponses, orderLineRequests)
        val order = Order.createOrder(orderItems)
        val savedOrder = orderRepository.save(order)
        return savedOrder.id!!
    }

    private fun createOrderLines(
        itemResponses: List<ItemResponse>,
        orderLineRequests: List<OrderLineRequest>
    ): List<OrderItem> {
        return orderLineRequests
            .map { request: OrderLineRequest -> createOrderLine(itemResponses, request) }
            .toList()
    }

    private fun createOrderLine(itemResponses: List<ItemResponse>, orderLineRequest: OrderLineRequest): OrderItem {

        val itemResponse = findItemDtoFromOrderLineRequest(itemResponses, orderLineRequest)

        return OrderItem(
            itemResponse.id,
            itemResponse.name,
            itemResponse.size,
            itemResponse.price,
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
            itemResponses: List<ItemResponse>,
            orderLineRequest: OrderLineRequest
        ): ItemResponse {
            return itemResponses.stream()
                .filter { it: ItemResponse -> it.id == orderLineRequest.itemId }
                .findAny()
                .orElseThrow { IllegalArgumentException("주문 항목 요청에 해당하는 상품이 없습니다.") }
        }
    }
}