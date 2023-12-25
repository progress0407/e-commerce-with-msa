package io.philo.shop.filter

import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class LoggingFilter : GlobalFilter, Ordered {

    private val log = KotlinLogging.logger { }

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {

        val request = exchange.request

        log.info {
            """
            [ Start ]
            id: ${request.id}  
            path: ${request.path}
            uri: ${request.uri}
            method: ${request.method}
            localAddress: ${request.localAddress}
            remoteAddress: ${request.remoteAddress}
        """.trimIndent()
        }

        return chain.filter(exchange).then(
            Mono.fromRunnable {
                val response = exchange.response
                log.info { """
                    [ End ]
                    id: ${request.id}
                    response.status: ${response.statusCode}
                    response.isCommitted: ${response.isCommitted}
                    rawStatusCode: ${response.rawStatusCode}
                """.trimIndent() }
            })
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}