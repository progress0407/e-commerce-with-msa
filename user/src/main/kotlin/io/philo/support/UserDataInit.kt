package io.philo.support

import io.philo.domain.entity.User
import io.philo.domain.repository.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class UserDataInit(private val userRepository: UserRepository) {

    /**
     * email: String,
     * name: String,
     * address: String,
     * rawPassword: String
     */
    @PostConstruct
    fun init() {

        val user = User("swcho@naver.com", "sw cho", address = "노량진", rawPassword = "12345678")

        userRepository.save(user)
    }
}