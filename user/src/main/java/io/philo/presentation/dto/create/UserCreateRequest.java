package io.philo.presentation.dto.create;

public record UserCreateRequest(
    String email,
    String name,
    String address,
    String password) {
}
