package io.philo.shop.filter

import com.google.gson.Gson
import io.philo.shop.user.UserRestClientFacade
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.context.annotation.Lazy
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * 인증 토큰 정보가 있을 경우
 *
 * User Passport 정보를 삽입하는 역할
 */
//@Component
class AuthorizationInceptionFilter(@Lazy private val userRestClient: UserRestClientFacade) : GlobalFilter, Ordered {

    private val log = KotlinLogging.logger { }

    private val gson = Gson()

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> {

        val request = exchange.request

        if ("/users/login" == request.path.toString())
            return proceedNextFilter(chain, exchange)

        val authorizationHeader: String = getAuthorizationHeader(request)

        val userPassport = userRestClient.getUserPassport(authorizationHeader)
        if (userPassport.isValid) {
            val jsonString = gson.toJson(userPassport)
            request.mutate().header("user-passport", jsonString).build()
        } else { // 예외의 경우!
            throw RuntimeException("올바르지 못한 인증 헤더입니다.")
        }

        return proceedNextFilter(chain, exchange)
    }

    private fun getAuthorizationHeader(request: ServerHttpRequest): String {
        val strings = request.headers[AUTHORIZATION]
        if (strings == null || strings.isEmpty()) {
            return ""
        }
        return strings[0].replace("Bearer ", "")
    }

    private fun proceedNextFilter(chain: GatewayFilterChain, exchange: ServerWebExchange): Mono<Void> {
        return chain.filter(exchange).then(Mono.fromRunnable { exchange.response })
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}