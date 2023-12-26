package io.philo.shop.user

import io.philo.shop.user.dto.UserPassportResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "user-service")
interface UserFeignClient {

    @GetMapping("/user/internal/valid-token")
    fun isValidToken(): Boolean

    @GetMapping("/user/internal/passport")
    fun getUserPassport(@RequestHeader(HttpHeaders.AUTHORIZATION) authHeader: String): UserPassportResponse

}