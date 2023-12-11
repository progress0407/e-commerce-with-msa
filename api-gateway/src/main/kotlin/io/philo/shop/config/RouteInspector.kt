package io.philo.shop.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.context.annotation.Configuration


@Configuration
class RouteInspector @Autowired constructor(routeLocator: RouteLocator) {
    init {
        routeLocator.routes.subscribe { route: Route ->
            println(
                "Route ID: " + route.id + ", URI: " + route.uri
            )
        }
    }
}