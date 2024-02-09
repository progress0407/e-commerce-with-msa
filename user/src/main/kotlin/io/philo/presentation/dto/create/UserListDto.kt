package io.philo.presentation.dto.create

import io.philo.domain.entity.UserEntity

class UserListDto

data class UserListResponseDto(val id: Long, val email: String, val name: String, val address: String) {

    constructor(entity: UserEntity) : this(entity.id!!, entity.email, entity.name, entity.address)
}