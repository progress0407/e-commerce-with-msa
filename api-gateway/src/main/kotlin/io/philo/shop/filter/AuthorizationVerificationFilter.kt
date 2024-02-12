package io.philo.shop.filter

import io.philo.shop.JwtManager
import io.philo.shop.constant.SecurityConstant.Companion.LOGIN_USER_ID
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

        val accessToken = validateAndExtractAccessToken(exchange)

        val userId = jwtManager.parse(accessToken)
//        val modifiedExchange = exchange setLoginUserId userId
        val modifiedRequest = exchange.request.mutate().header(LOGIN_USER_ID, userId).build()
        val modifiedExchange = exchange.mutate().request(modifiedRequest).build()

        return proceedNextFilter(chain, modifiedExchange)
    }

    override fun getOrder(): Int {
        return 2
    }

    private infix fun ServerWebExchange.setLoginUserId(userId: String): ServerWebExchange {

        val request = this.request.mutate().header(LOGIN_USER_ID, userId).build()
        return this.mutate().request(request).build()
    }

    private fun setLoginUserId(request: ServerHttpRequest, userId: String) {
        request.mutate().header(LOGIN_USER_ID, userId).build()
    }
}
