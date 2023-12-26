package io.philo.presentation

import io.philo.domain.service.UserService
import io.philo.shop.user.dto.UserPassportResponse
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/internal")
class UserInternalController(private val userService: UserService) {

    val log = KotlinLogging.logger { }

    @GetMapping("/valid-token")
    fun isValidToken(@RequestHeader(HttpHeaders.AUTHORIZATION) acceeToekn: String): Boolean {

        return userService.isValidToken(acceeToekn)
    }

    @GetMapping("/passport")
    fun passport(@RequestHeader(HttpHeaders.AUTHORIZATION) tokens: List<String>): UserPassportResponse {

        val accessToken = tokens[0]

        log.info { "accessToken=$accessToken" }

        return userService.passport(accessToken)
    }

    @GetMapping("/test")
    fun test(): String {

        return "hello seomthign"
    }

}
