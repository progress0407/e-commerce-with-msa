package io.philo.presentation.dto.create

data class UserCreateRequest(
    val email: String,
    val name: String,
    val address: String,
    val password: String
)
