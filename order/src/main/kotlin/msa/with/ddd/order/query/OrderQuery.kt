package msa.with.ddd.order.query

import msa.with.ddd.order.domain.Order
import msa.with.ddd.order.dto.web.OrderDetailResponse
import msa.with.ddd.order.dto.web.OrderListResponse
import msa.with.ddd.order.dto.web.OrderListResponses
import msa.with.ddd.order.repository.OrderRepository
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class OrderQuery(private val orderRepository: OrderRepository) {

    fun list(): OrderListResponses {
        val savedItems = orderRepository.findAll()
        return convertListDtos(savedItems);
    }

    fun detail(id: Long): OrderDetailResponse {
        val entitiy = orderRepository.findById(id).orElseThrow { IllegalArgumentException("존재하지 않는 주문입니다.") }
        return OrderDetailResponse(entitiy)
    }

    private fun convertListDtos(savedItems: List<Order>): OrderListResponses {
        val dtos = savedItems
            .map(::OrderListResponse)
            .toList()
        return OrderListResponses(dtos)
    }
}