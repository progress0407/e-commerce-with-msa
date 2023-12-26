package io.philo.presentation

import io.philo.domain.service.UserService
import io.philo.presentation.dto.create.UserCreateRequest
import io.philo.presentation.dto.create.UserCreateResponse
import io.philo.presentation.dto.login.UserLoginRequest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping("/test")
    fun test(): String {
        return "ok"
    }


    @PostMapping
    fun create(@RequestBody request: UserCreateRequest): UserCreateResponse {

        val userId = userService.createUser(
            request.email,
            request.name,
            request.address,
            request.password
        )

        return UserCreateResponse(userId)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<*> {

        val accessToken = userService.login(request.email, request.password)

        return ResponseEntity.ok()
            .header(AUTHORIZATION, accessToken)
            .body("User logged in successfully. See response header")
    }
}
