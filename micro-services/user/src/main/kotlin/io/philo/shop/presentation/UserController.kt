package io.philo.shop.presentation

import io.philo.shop.domain.repository.UserRepository
import io.philo.shop.domain.service.UserService
import io.philo.shop.presentation.dto.create.UserCreateRequestDto
import io.philo.shop.presentation.dto.create.UserCreateResponseDto
import io.philo.shop.presentation.dto.create.UserListResponseDto
import io.philo.shop.presentation.dto.login.UserLoginRequest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
) {

    @PostMapping("/login")
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<*> {

        val accessToken = userService.login(request.email, request.password)

        return ResponseEntity.ok()
            .header(AUTHORIZATION, accessToken)
            .body("User logged in successfully. See response header")
    }

    @PostMapping
    fun create(@RequestBody request: UserCreateRequestDto): UserCreateResponseDto {

        val userId = userService.createUser(
            request.email,
            request.name,
            request.address,
            request.password
        )

        return UserCreateResponseDto(userId)
    }

    /**
     * todo! 페이징 처리하기
     */
    @GetMapping
    fun list(): List<UserListResponseDto> {
        val entities = userRepository.findAll()

        return entities
            .map { UserListResponseDto(it!!) }
            .toList()
    }
}
