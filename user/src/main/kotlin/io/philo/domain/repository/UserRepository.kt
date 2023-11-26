package io.philo.domain.repository

import io.philo.domain.entity.User
import io.philo.shop.error.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User?, Long?> {
    fun findByEmail(email: String): User?
}
