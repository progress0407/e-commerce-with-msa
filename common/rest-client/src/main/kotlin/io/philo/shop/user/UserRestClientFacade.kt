package io.philo.shop.user

import io.philo.shop.user.dto.UserPassportResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class UserRestClientFacade(private val userFeignClient: UserFeignClient) {

    val log = KotlinLogging.logger { }

    fun isValidToken(): Boolean {
        return userFeignClient.isValidToken()
    }

    fun getUserPassport(authHeader: List<String>): UserPassportResponse {
        return userFeignClient.getUserPassport(authHeader)
    }

    fun test(): String {
        return userFeignClient.test()
    }
}