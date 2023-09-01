package msa.with.ddd.order.ui

import msa.with.ddd.order.application.OrderService
import msa.with.ddd.order.dto.web.CreateOrderRequest
import lombok.RequiredArgsConstructor
import msa.with.ddd.order.dto.web.OrderDetailResponse
import msa.with.ddd.order.dto.web.OrderListResponses
import msa.with.ddd.order.query.OrderQuery
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController(private val orderService: OrderService,
                      private val orderQuery: OrderQuery) {

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