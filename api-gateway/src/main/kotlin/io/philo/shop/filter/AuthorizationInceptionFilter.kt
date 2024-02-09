package io.philo.shop.filter

import com.google.gson.Gson
import io.philo.shop.error.UnauthorizedException
import io.philo.shop.user.UserRestClientFacade
import io.philo.shop.user.dto.UserPassportResponse
import mu.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.context.annotation.Lazy
import org.springframework.core.Ordered
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


/**
 * 인증 토큰 정보가 있을 경우
 *
 * User Passport 정보를 삽입하는 역할
 *
 * Netflix Passport에서 착안한 아이디어
 */
//@Component
@Deprecated("동기 호출로 인한 Blocking RuntTime 예외로 잠정 중단")
class AuthorizationInceptionFilter(@Lazy private val userRestClient: UserRestClientFacade) :
    AbstractAuthorizationFilter(), GatewayFilter, Ordered {

    private val log = KotlinLogging.logger { }

    private val gson = Gson()

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain,
    ): Mono<Void> {

        val request = exchange.request

//        if ("/users/login" == request.path.toString())
//            return proceedNextFilter(chain, exchange)

        val accessToken = validateAndExtractAccessToken(request)
        val userPassport = userRestClient.getUserPassport(accessToken)

        validatePassport(userPassport)

        request.setUserPassport(userPassport)

        return proceedNextFilter(chain, exchange)
    }

    private fun validatePassport(userPassport: UserPassportResponse) {
        if (userPassport.isValid.not())
            throw UnauthorizedException("올바르지 못한 인증 헤더입니다.")
    }

    /**
     * Request 객체에 유저 패스포트 저장
     */
    private fun ServerHttpRequest.setUserPassport(userPassport: UserPassportResponse) {
        val jsonString = gson.toJson(userPassport)
        this.mutate().header(USER_PASSPORT, jsonString).build()
    }

    override fun getOrder(): Int {
        return Ordered.LOWEST_PRECEDENCE
    }
}
