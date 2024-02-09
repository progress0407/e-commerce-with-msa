package io.philo.shop.config

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.http.HttpMethod

/**
 * 동작했던 설정
 */
//@Configuration
class RouteConfigWorked {

//    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator {

        return builder.routes()
            .route { it.simpleRoute("USER-SERVICE", "users") }
            .build()
    }

    /**
     * todo filters 구현
     */
    private fun PredicateSpec.simpleRoute(serviceName: String, path: String, filter: GatewayFilter? = null, vararg methods: HttpMethod = emptyArray()): Buildable<Route> {

        val pathSpec = this.path("/$path/**")

        if (methods.isNotEmpty())
            pathSpec.and().method(*methods)

        return pathSpec
            .filters { filterSpec: GatewayFilterSpec ->

                // 아무 설정 X -> /users/users/login 로 입력해야 한다
                filterSpec.removeRequestHeader("Cookie")
                    .rewritePath("/$path/(?<segment>.*)", "/\${segment}") // ex. /users/test -> /test

                if(filter != null)
                    filterSpec.filters(filter)

                filterSpec
            }
            .uri("lb://${serviceName}")
    }
}