package msa.with.ddd

import msa.with.ddd.LoggingFilter.Config
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LoggingFilter : AbstractGatewayFilterFactory<Config>(Config::class.java) {

    val log = LoggerFactory.getLogger(LoggingFilter::class.java)!!

    override fun apply(config: Config?): GatewayFilter =
        OrderedGatewayFilter({ exchange, chain ->
            if (config == null) {
                throw NullPointerException("config should not be null")
            }

            log.info("Logging Filter baseMessage: {}", config.baseMessage)

            if (config.preLogger) {
                log.info("Logging Filter Start: request id -> {}", exchange.request.id)
            }

            chain
                .filter(exchange)
                .then(Mono.fromRunnable {
                    if (config.postLogger) {
                        log.info("Logging Filter End: request id -> {}", exchange.response.statusCode)
                    }
                })
        }, Ordered.LOWEST_PRECEDENCE)

    class Config {
        lateinit var baseMessage: String
        var preLogger: Boolean = false
        var postLogger: Boolean = false
    }
}
