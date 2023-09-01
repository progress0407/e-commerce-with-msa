package msa.with.ddd

import mu.KLogger
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class LoggingFilter(
    private val preLogger: Boolean = true,
    private val postLogger: Boolean = true,
    private val baseMessage: String = "Logging Filter baseMessage"
) : GatewayFilter, Ordered {

    private val log = KotlinLogging.logger { }

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        log.info("Logging Filter baseMessage: {}", baseMessage)

        if (preLogger) {
            log.info("Logging Filter Start: request id -> {}", exchange.request.id)
        }

        return chain.filter(exchange).then(
            Mono.fromRunnable {
                if (postLogger) {
                    log.info("Logging Filter End: response status -> {}", exchange.response.statusCode)
                }
            })
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}
