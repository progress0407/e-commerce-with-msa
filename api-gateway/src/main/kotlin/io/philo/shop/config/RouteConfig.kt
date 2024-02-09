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
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST

@Configuration
class RouteConfig {

    @Bean
    fun routes(builder: RouteLocatorBuilder, loggingFilter: LoggingFilter): RouteLocator {

        return builder.routes()
            .route { it.route("ITEM-SERVICE", "/items") }
            .route { it.route("ORDER-SERVICE", "/orders", httpMethods = arrayOf(POST, GET)) }
            .route { it.route("USER-SERVICE", "/users") }
//            .route { it.path("").filters { it.filters(authorizationInceptionFilter)}.uri("")}
            .build()
    }

    private fun PredicateSpec.route(serviceName: String, path: String, vararg httpMethods: HttpMethod): Buildable<Route> {

        return if (httpMethods == null) {
            this.path("$path/**")
                .filters { it.buildFilter(path) }
                .uri("lb://${serviceName}")
        } else {
            this.path("$path/**")
                .and().method(*httpMethods)
                .filters { it.buildFilter(path) }
                .uri("lb://${serviceName}")
        }
    }

    private fun GatewayFilterSpec.buildFilter(path: String): GatewayFilterSpec =
        this.removeRequestHeader("Cookie")
//            .filter(loggingFilter)
//            .filters(authorizationInceptionFilter)
}
