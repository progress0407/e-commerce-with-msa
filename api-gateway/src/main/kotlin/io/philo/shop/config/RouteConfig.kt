package io.philo.shop.config

import io.philo.shop.filter.AuthorizationVerificationFilter
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
    private val authFilter: AuthorizationVerificationFilter,
) {

    @Bean
    fun routes(): RouteLocator =
        routeLocatorBuilder.routes()
            .route { it.route(serviceName = "USER-SERVICE", path = "/users") }

            .route { it.route(serviceName = "ITEM-SERVICE", path = "/items", httpMethods = arrayOf(GET)) }
            .route { it.route(serviceName = "ITEM-SERVICE", path = "/items", httpMethods = arrayOf(POST), authRequired = true) }

            .route { it.route(serviceName = "ORDER-SERVICE", path = "/orders", httpMethods = arrayOf(POST, GET), authRequired = true) }

            .build()

    private fun PredicateSpec.route(
        serviceName: String,
        path: String,
        vararg httpMethods: HttpMethod,
        authRequired: Boolean = false,
    ): Buildable<Route> {

        val pathSpec = pathSpec(path, httpMethods)

        return pathSpec
            .filters { it.buildFilter(path, authRequired) }
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


    private fun GatewayFilterSpec.buildFilter(path: String, authRequired: Boolean = false): GatewayFilterSpec {

        val filterSpec = this.removeRequestHeader("Cookie")
//            .rewritePath("/$path/(?<segment>.*)", "/\${segment}") // ex. /users/test -> /test

        if (authRequired.not())
            return filterSpec

        return filterSpec.filter(authFilter)
    }
}
