package io.philo.shop.filter

import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * 인증 토큰 정보가 있을 경우
 *
 * User Passport 정보를 삽입하는 역할
 */
@Component
class AuthorizationInceptionFilter : GlobalFilter, Ordered {

    private val log = KotlinLogging.logger { }

    override fun filter(exchange: ServerWebExchange,
                        chain: GatewayFilterChain): Mono<Void> {

        val request = exchange.request

        val authorizationHeader: MutableList<String>? = request.headers[HttpHeaders.AUTHORIZATION]

        // null 도 아니고 비어있으면 안돼, 유효한 헤더여야만해!
        if (authorizationHeader != null && authorizationHeader.isNotEmpty()) {
            request.mutate().header("user-passport", "hello-something-do-that").build()
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