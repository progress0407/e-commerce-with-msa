package io.philo.shop.integartion

import io.philo.shop.AcceptanceTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import reactor.core.publisher.Flux

/**
 * 라우팅이 잘 이루어졌는지 테스트
 */
class RoutingMappingTest : AcceptanceTest() {

//    @Autowired
//    lateinit var routeDefinitionLocator: RouteDefinitionLocator

    @Autowired
    lateinit var routeLocator: RouteLocator


    @Test
    fun `라우트 목록을 테스트한다`() {
//        val routeDefinitions = routeDefinitionLocator.getRouteDefinitions()
//        println("routeDefinitionLocator = ${routeDefinitionLocator}")

        val routes: Flux<Route> = routeLocator.routes
        println("routes = ${routes}")
    }


}