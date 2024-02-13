package io.philo.shop.ui

import io.philo.shop.application.OrderService
import io.philo.shop.constant.SecurityConstant.Companion.LOGIN_USER_ID
import io.philo.shop.dto.ResourceCreateResponse
import io.philo.shop.dto.web.OrderCreateRequest
import io.philo.shop.dto.web.OrderDetailResponse
import io.philo.shop.dto.web.OrderListResponses
import io.philo.shop.query.OrderQuery
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController(private val orderService: OrderService,
                      private val orderQuery: OrderQuery
) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun order(@RequestBody request: OrderCreateRequest,
              @RequestHeader(LOGIN_USER_ID) requesterId: Long): ResourceCreateResponse {

        val orderLineRequests = request.orderLineRequestDtos
        val orderId = orderService.order(orderLineRequests, requesterId)

        return ResourceCreateResponse(orderId)
    }

    @GetMapping
    fun list(): OrderListResponses {

        return orderQuery.list()
    }

    @GetMapping("/id")
    fun detail(@PathVariable("id") id: Long): OrderDetailResponse {

        return orderQuery.detail(id)
    }
}