package io.philo.shop.config

import io.philo.shop.filter.GlobalLoggingFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder

//@Configuration
class RouteConfig_backup(private val globalLoggingFilter: GlobalLoggingFilter) {

//    @Bean
    fun routes(builder: RouteLocatorBuilder, globalLoggingFilter: GlobalLoggingFilter): RouteLocator {
        return builder.routes()
//            .route { it.route("ITEM-SERVICE", "/items") }
            .route { it.route("ITEM-SERVICE", "/items") }
            .route { it.route("ORDER-SERVICE", "/orders") }
            .build()
    }

    private fun PredicateSpec.route(serviceName: String, path: String): Buildable<Route> =
        this.path("$path/**")
            .filters { it.buildFilter(path) }
            .uri("lb://${serviceName}")

    private fun GatewayFilterSpec.buildFilter(path: String): GatewayFilterSpec =
        this.removeRequestHeader("Cookie")
//            .filter(loggingFilter)
            /**
             * success  ex. /item-service/items/1 -> /items/1
             * fail     ex. /items -> /
             */
//            .rewritePath("$path/(?<segment>.*)", "/\${segment}")
            /**
             * success  ex. /items/1 -> /items/1
             * fail  ex. /item-service/items/1 -> /items/1
             */
//            .rewritePath(path, "/")
}
