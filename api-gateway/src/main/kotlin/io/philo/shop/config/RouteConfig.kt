package io.philo.shop.config

import io.philo.shop.filter.LoggingFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteConfig {

    @Bean
    fun routes(builder: RouteLocatorBuilder, loggingFilter: LoggingFilter): RouteLocator {

        return builder.routes()
            .route { it.route("ITEM-SERVICE", "/items") }
            .route { it.route("ORDER-SERVICE", "/orders") }
//            .route { it.path("").filters { it.filters(authorizationInceptionFilter)}.uri("")}
            .build()
    }

    private fun PredicateSpec.route(serviceName: String, path: String): Buildable<Route> =
        this.path("$path/**")
            .filters { it.buildFilter(path) }
            .uri("lb://${serviceName}")

    private fun GatewayFilterSpec.buildFilter(path: String): GatewayFilterSpec =
        this.removeRequestHeader("Cookie")
//            .filter(loggingFilter)
//            .filters(authorizationInceptionFilter)
}
