package io.philo.shop.presentation

import io.philo.shop.domain.service.UserService
import io.philo.shop.user.dto.UserPassportResponse
import mu.KotlinLogging
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/internal")
class UserInternalController(private val userService: UserService) {

    val log = KotlinLogging.logger { }

    @GetMapping("/valid-token")
    fun isValidToken(@RequestHeader(AUTHORIZATION) accessToken: String): Boolean {

        return userService.isValidToken(accessToken)
    }

    @GetMapping("/passport")
    fun passport(@RequestHeader(AUTHORIZATION) accessToken: String): UserPassportResponse {

        log.info { "accessToken=$accessToken" }

        return userService.passport(accessToken)
    }
}
