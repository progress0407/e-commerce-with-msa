package io.philo.shop.presentation

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RefreshScope
class ApiGatewayController(
    @Value("\${constant.test.value}")
    private val testConstantValue: String,
    private val routeLocator: RouteLocator
) {

    @GetMapping("/gw/env-test")
    fun testConstantValue(): String {
        return "check value for dynamical refresh test: $testConstantValue"
    }

    @GetMapping("/route-info")
    fun routeLocator(): RouteLocator {
        return routeLocator
    }
}
