package io.philo.shop.integartion

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

/**
 * 라우팅이 잘 이루어졌는지 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoutingMappingTest {

    @LocalServerPort
    var port = 0

//    @Autowired
//    lateinit var routeDefinitionLocator: RouteDefinitionLocator

//    @Autowired
//    lateinit var routeLocator: RouteLocator

    @BeforeEach
    protected fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun `라우트 목록을 테스트한다`() {
        println("")
//        val routeDefinitions = routeDefinitionLocator.getRouteDefinitions()
//        println("routeDefinitionLocator = ${routeDefinitionLocator}")

//        val routes: Flux<Route> = routeLocator.routes
//        println("routes = ${routes}")
    }
}