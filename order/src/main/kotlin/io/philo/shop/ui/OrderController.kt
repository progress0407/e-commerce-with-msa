package io.philo.shop.ui

import io.philo.shop.application.OrderService
import io.philo.shop.dto.web.CreateOrderRequest
import io.philo.shop.dto.web.OrderDetailResponse
import io.philo.shop.dto.web.OrderListResponses
import io.philo.shop.query.OrderQuery
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController(private val orderService: OrderService,
                      private val orderQuery: OrderQuery
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun order(@RequestBody request: CreateOrderRequest): Long {
        return orderService.order(request)
    }

    @GetMapping
    @ResponseStatus(OK)
    fun list(): OrderListResponses {
        return orderQuery.list()
    }

    @GetMapping("/id")
    @ResponseStatus(OK)
    fun detail(@PathVariable("id") id: Long): OrderDetailResponse {
        return orderQuery.detail(id)
    }
}