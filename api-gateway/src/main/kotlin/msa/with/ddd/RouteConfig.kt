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

//    @Bean
    fun routes(builder: RouteLocatorBuilder, loggingFilter: LoggingFilter): RouteLocator {
        return builder.routes()
            .route { simpleRouteBuildable(it, "item-service") }
            .route { simpleRouteBuildable(it, "order-service") }
            .build()
    }

    private fun simpleRouteBuildable(predicate: PredicateSpec, url: String): Buildable<Route> =
        predicate.path("/$url/**")
            .filters { filter ->
                filter.removeRequestHeader("Cookie")
                    // - RewritePath=/user-service/(?<segment>.*),/$\{segment}
//                    .rewritePath("/$url/(?<segment>.*)", "/\${segment}")
                    .rewritePath("/$url/(?<path>.*)", "/\${path}")
                    .filter(loggingFilter)
            }
            .uri("lb://${url.uppercase()}")
}
