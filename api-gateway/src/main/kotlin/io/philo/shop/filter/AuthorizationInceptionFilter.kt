package io.philo.shop.filter

import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * 인증 토큰 정보가 있을 경우
 *
 * 유저 패스포트 정보를 삽입하는 역할
 */
@Component
class AuthorizationInceptionFilter : GatewayFilter, Ordered {

    private val log = KotlinLogging.logger { }

    override fun filter(exchange: ServerWebExchange,
                        chain: GatewayFilterChain): Mono<Void> {

        val request = exchange.request

        val authorizationHeader: MutableList<String>? = request.headers[HttpHeaders.AUTHORIZATION]

        if (authorizationHeader.isNullOrEmpty()) {
            return proceedNextFilter(chain, exchange)
        }

        return proceedNextFilter(chain, exchange)
    }

    private fun proceedNextFilter(
        chain: GatewayFilterChain,
        exchange: ServerWebExchange
    ): Mono<Void> = chain.filter(exchange).then(Mono.fromRunnable { exchange.response })

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}