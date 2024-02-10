package io.philo.shop.filter

import io.philo.shop.error.UnauthorizedException
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

abstract class AbstractAuthorizationFilter {

    companion object {
        @JvmStatic
        protected val TOKEN_PREFIX = "Bearer "

        @JvmStatic
        protected val USER_PASSPORT = "user-passport"

        @JvmStatic
        protected val LOGIN_USER_ID = "loginUserId"
    }

    /**
     * JWT 에 대해 유효성 검증을 하고 토큰을 추출합니다
     */
    protected open fun validateAndExtractAccessToken(request: ServerHttpRequest): String {

        val headerValues = request.headers[HttpHeaders.AUTHORIZATION]

        if (headerValues.isNullOrEmpty())
            throw UnauthorizedException("Bearer prefix가 존재하지 않습니다.")

        val tokenWithPrefix = headerValues
            .find { it.contains(TOKEN_PREFIX) }
            ?: throw UnauthorizedException("Bearer prefix가 존재하지 않습니다.")

        return tokenWithPrefix.replace(TOKEN_PREFIX, "")
    }

    protected fun proceedNextFilter(chain: GatewayFilterChain, exchange: ServerWebExchange): Mono<Void> {
        return chain.filter(exchange).then(Mono.fromRunnable { exchange.response })
    }
}