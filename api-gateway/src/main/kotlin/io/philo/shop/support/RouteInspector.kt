package io.philo.shop.support

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.context.annotation.Configuration


@Configuration
class RouteInspector @Autowired constructor(routeLocator: RouteLocator) {

    private val log = KotlinLogging.logger { }

    init {
        routeLocator.routes.subscribe { route: Route ->
            log.info { "Route ID: ${route.id} , URI: ${route.uri}" }
        }
    }
}