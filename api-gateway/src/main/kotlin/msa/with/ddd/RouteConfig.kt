package msa.with.ddd

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteConfig(private val loggingFilter: LoggingFilter) {

    @Bean
    fun routes(builder: RouteLocatorBuilder, loggingFilter: LoggingFilter): RouteLocator {
        return builder.routes()
            .route { it.simpleRouteBuildable("ITEM-SERVICE", "/items") }
            .route { it.simpleRouteBuildable("ORDER-SERVICE", "/orders") }
            .build()
    }

    private fun PredicateSpec.simpleRouteBuildable(serviceName: String, url: String): Buildable<Route> =
        this.path("$url/**")
            .filters { filter ->
                filter.removeRequestHeader("Cookie")
//                    .rewritePath("/$url/(?<path>.*)", "/\${path}")
                    .filter(loggingFilter)
            }
            .uri("lb://${serviceName}")
}
