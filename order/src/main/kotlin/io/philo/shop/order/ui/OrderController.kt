package io.philo.shop.order.ui

import io.philo.shop.order.application.OrderService
import io.philo.shop.order.dto.web.CreateOrderRequest
import io.philo.shop.order.dto.web.OrderDetailResponse
import io.philo.shop.order.dto.web.OrderListResponses
import io.philo.shop.order.query.OrderQuery
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController(private val orderService: OrderService,
                      private val orderQuery: OrderQuery
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun order(@RequestBody request: CreateOrderRequest): Long {
        return orderService.order(request)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun list(): OrderListResponses {
        return orderQuery.list()
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    fun detail(@PathVariable("id") id: Long): OrderDetailResponse {
        return orderQuery.detail(id)
    }
}