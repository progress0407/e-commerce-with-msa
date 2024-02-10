package io.philo.shop.application

import io.philo.shop.coupon.CouponRestClientFacade
import io.philo.shop.domain.Order
import io.philo.shop.domain.OrderItem
import io.philo.shop.dto.web.OrderLineRequest
import io.philo.shop.error.BadRequestException
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
    private val couponClient: CouponRestClientFacade,
    private val orderEventPublisher: OrderEventPublisher
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun order(orderLineRequests: List<OrderLineRequest>): Long {

        validateCoupoUseable(orderLineRequests)

        val itemIds = extractItemIds(orderLineRequests)
        val itemResponses = itemClient.requestItems(itemIds)
        val discountAmountMap = couponClient.requestItemCostsByIds(itemIds)

        val orderItems = createOrderLines(itemResponses, orderLineRequests,discountAmountMap)
        val order = Order.createOrder(orderItems)
        orderRepository.save(order)

        /**
         * 결제가 성공한다면 아래 로직 수행?
         *
         * DB 커넥션을 아끼고 싶다면,
         *
         * 비동기로 할 것.
         */

        orderEventPublisher.publishEvent(order)

        return order.id!!
    }

    /**
     * 쿠폰, 상품 수량 검증.
     *
     * 쿠폰은 하나의 상품에 대해서만 사용할 수 있습니다.
     */
    private fun validateCoupoUseable(orderLineRequests: List<OrderLineRequest>) {
        for (orderLineRequest in orderLineRequests) {
            if (orderLineRequest.userCouponId != null && orderLineRequest.quantity != 1) {
                throw BadRequestException("쿠폰은 하나의 상품에만 적용할 수 있습니다.")
            }
        }
    }

    private fun extractItemIds(orderLineRequests: List<OrderLineRequest>): List<Long> {
        return orderLineRequests
            .map(OrderLineRequest::itemId)
            .toList()
    }

    private fun createOrderLines(
        itemResponses: List<ItemInternalResponse>,
        orderLineRequests: List<OrderLineRequest>,
        discountAmountMap: Map<Long, Int>
    ): MutableList<OrderItem> {
        return orderLineRequests
            .map { request: OrderLineRequest -> createOrderLine(itemResponses, request, discountAmountMap) }
            .toMutableList()
    }

    private fun createOrderLine(
        itemResponses: List<ItemInternalResponse>,
        orderLineRequest: OrderLineRequest,
        discountAmountMap: Map<Long, Int>
    ): OrderItem {
        val itemResponse = findItemDtoFromOrderLineRequest(itemResponses, orderLineRequest)
        val itemId = itemResponse.id
        return OrderItem(
            itemId = itemId,
            itemName = itemResponse.name,
            size = itemResponse.size,
            orderItemPrice = discountAmountMap[itemId]!!,
            orderedQuantity = orderLineRequest.quantity
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