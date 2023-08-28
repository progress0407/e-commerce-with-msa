package msa.with.ddd.order.ui

import msa.with.ddd.order.application.OrderService
import msa.with.ddd.order.dto.web.CreateOrderRequest
import lombok.RequiredArgsConstructor
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
    fun list() {
        orderQuery.list()
    }

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    fun detail(@RequestBody request: CreateOrderRequest): Long {
        return 1
    }
}