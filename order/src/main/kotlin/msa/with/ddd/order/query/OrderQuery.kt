package msa.with.ddd.order.query

import msa.with.ddd.order.domain.Order
import msa.with.ddd.order.dto.web.OrderListResponse
import msa.with.ddd.order.dto.web.OrderListResponses
import msa.with.ddd.order.repository.OrderRepository
import org.springframework.stereotype.Component

@Component
class OrderQuery(private val orderRepository: OrderRepository) {

    fun list() {
        val savedItems = orderRepository.findAll()
        convertListDtos(savedItems);

    }

    private fun convertListDtos(savedItems: List<Order>): OrderListResponses {
        val dtos = savedItems
            .map(::OrderListResponse)
            .toList()
        return OrderListResponses(dtos)
    }
}