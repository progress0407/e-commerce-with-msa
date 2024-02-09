package io.philo.presentation.dto.create

class UserCreateDto

data class UserCreateRequestDto(
    val email: String,
    val name: String,
    val address: String,
    val password: String
) {
    companion object
}

data class UserCreateResponseDto(val id: Long)
