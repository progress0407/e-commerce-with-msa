package io.philo.shop.config

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST

@Configuration
class RouteConfig(
    private val routeLocatorBuilder: RouteLocatorBuilder,
) {

//    @Bean
//    @Order(-1)
//    fun globalLoggingFilter() = GlobalLoggingFilter()

    @Bean
    fun routes(): RouteLocator {

        return routeLocatorBuilder.routes()
            .route { it.route("USER-SERVICE", "/users") }
            .route { it.route("ITEM-SERVICE", "/items") }
            .route { it.route("ORDER-SERVICE", "/orders", httpMethods = arrayOf(POST, GET)) }
            .build()
    }

    private fun PredicateSpec.route(
        serviceName: String,
        path: String,
        vararg httpMethods: HttpMethod,
    ): Buildable<Route> {

        val pathSpec = pathSpec(path, httpMethods)

        return pathSpec
            .filters { it.removeRequestHeader("Cookie") }
            .uri("lb://${serviceName}")
    }

    private fun PredicateSpec.pathSpec(
        path: String,
        httpMethods: Array<out HttpMethod>,
    ): BooleanSpec {
        val pathSpec: BooleanSpec = this.path("$path/**")

        if (httpMethods.isEmpty())
            return pathSpec

        return pathSpec.and().method(*httpMethods)
    }


    private fun GatewayFilterSpec.buildFilter(path: String): GatewayFilterSpec =
        this.removeRequestHeader("Cookie")
    //            .rewritePath("/$path/(?<segment>.*)", "/\${segment}") // ex. /users/test -> /test
    //            .filters(authorizationInceptionFilter)
}
