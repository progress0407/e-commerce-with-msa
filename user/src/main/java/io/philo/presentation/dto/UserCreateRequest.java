package io.philo.presentation.dto;

public record UserCreateRequest(
    String email,
    String name,
    String address,
    String password) {
}
