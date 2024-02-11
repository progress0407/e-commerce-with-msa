package io.philo.shop.query

import io.philo.shop.domain.core.OrderEntity
import io.philo.shop.dto.web.OrderDetailResponse
import io.philo.shop.dto.web.OrderListResponse
import io.philo.shop.dto.web.OrderListResponses
import io.philo.shop.repository.OrderRepository
import org.springframework.stereotype.Component

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

    private fun convertListDtos(savedItems: List<OrderEntity>): OrderListResponses {
        val dtos = savedItems
            .map(::OrderListResponse)
            .toList()
        return OrderListResponses(dtos)
    }
}