package io.philo.shop.filter

import io.philo.shop.user.UserRestClientFacade
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Lazy
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
class AuthorizationInceptionFilter(@Lazy private val userRestClient: UserRestClientFacade) : GlobalFilter, Ordered {

    private val log = KotlinLogging.logger { }

    override fun filter(exchange: ServerWebExchange,
                        chain: GatewayFilterChain): Mono<Void> {

        val request = exchange.request

        val authorizationHeaders: MutableList<String>? = request.headers[HttpHeaders.AUTHORIZATION]

        // null 도 아니고 비어있으면 안돼, 유효한 헤더여야만해!
        if (authorizationHeaders != null && authorizationHeaders.isNotEmpty()) {
            val userPassport = userRestClient.getUserPassport(authorizationHeaders)

            if (userPassport.isValid) {
                request.mutate().header("user-passport", "hello-something-do-that").build()
            } else { // 예외의 경우!
                throw RuntimeException("올바르지 못한 인증 헤더입니다.")
            }
//            val userPassport = userRestClient.test()
//            println("userPassport = ${userPassport}")
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