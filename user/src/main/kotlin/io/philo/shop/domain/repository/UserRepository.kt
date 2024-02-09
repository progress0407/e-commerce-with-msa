package io.philo.shop.domain.repository

import io.philo.shop.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity?, Long?> {
    fun findByEmail(email: String): UserEntity?
}
