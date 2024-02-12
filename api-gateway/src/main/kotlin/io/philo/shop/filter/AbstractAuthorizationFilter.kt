package io.philo.shop.filter

import io.philo.shop.constant.SecurityConstant.Companion.TOKEN_PREFIX
import io.philo.shop.error.UnauthorizedException
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpHeaders
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

abstract class AbstractAuthorizationFilter {


    private val log = KotlinLogging.logger { }

    /**
     * JWT 에 대해 유효성 검증을 하고 토큰을 추출합니다
     */
    protected open fun validateAndExtractAccessToken(exchange: ServerWebExchange): String {

        val request = exchange.request
        val headerValues = request.headers[HttpHeaders.AUTHORIZATION]

        if (headerValues.isNullOrEmpty())
            throw UnauthorizedException("Bearer prefix가 존재하지 않습니다.")

        val tokenWithPrefix = findOrThrow(headerValues)

        return formatToken(tokenWithPrefix)
    }

    private fun formatToken(tokenWithPrefix: String) = tokenWithPrefix
        .replace(TOKEN_PREFIX, "")
        .removeAllWhiteSpaces()
        .trim()

    private fun findOrThrow(headerValues: List<String>) = (headerValues
        .find { it.contains(TOKEN_PREFIX) }
        ?: throw UnauthorizedException("Bearer prefix가 존재하지 않습니다."))

    protected fun proceedNextFilter(chain: GatewayFilterChain, exchange: ServerWebExchange): Mono<Void> {
//        return chain.filter(exchange)
        return chain.filter(exchange).then(Mono.fromRunnable {
            exchange.response

            log.info { "this is Auth Post Process" }
        })
    }

    private fun String.removeAllWhiteSpaces() = this.replace("\\s+", "")
}