package io.philo.shop.presentation.dto.login

data class UserLoginRequest(
    val email: String,
    val password: String
) {
    companion object
}

