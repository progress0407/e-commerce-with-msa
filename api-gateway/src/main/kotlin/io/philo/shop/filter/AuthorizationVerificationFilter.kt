package io.philo.shop.filter

import io.philo.shop.JwtManager
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.Ordered
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * API Gateway 기본 Edge 기능인 인증/인가 필터
 */
@Component
class AuthorizationVerificationFilter(private val jwtManager: JwtManager) : AbstractAuthorizationFilter(), GatewayFilter, Ordered {


    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {

        val request = exchange.request
        val accessToken = validateAndExtractAccessToken(request)
        jwtManager.isValidToken(accessToken)

        val userId = jwtManager.parse(accessToken)

        setLoginUserId(request, userId)

        return proceedNextFilter(chain, exchange)
    }

    private fun setLoginUserId(request: ServerHttpRequest, userId: String) {
        request.mutate().header(LOGIN_USER_ID, userId).build()
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}