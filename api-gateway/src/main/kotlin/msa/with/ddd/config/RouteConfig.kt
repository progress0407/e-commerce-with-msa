package msa.with.ddd.config

import msa.with.ddd.filter.LoggingFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteConfig(private val loggingFilter: LoggingFilter) {

    @Bean
    fun routes(builder: RouteLocatorBuilder, loggingFilter: LoggingFilter): RouteLocator {
        return builder.routes()
            .route { it.simpleRoute("ITEM-SERVICE", "/items") }
            .route { it.simpleRoute("ORDER-SERVICE", "/orders") }
            .build()
    }

    private fun PredicateSpec.simpleRoute(serviceName: String, url: String): Buildable<Route> =
        this.path("$url/**")
            .filters { filter -> buildFilter(filter, url) }
            .uri("lb://${serviceName}")

    private fun buildFilter(filter: GatewayFilterSpec, url: String): GatewayFilterSpec? =
        filter.removeRequestHeader("Cookie")
            .rewritePath("$url(?<segment>/?.*)", "$\\{segment}") // ex. /order/1 -> /1
            .filter(loggingFilter)
}
