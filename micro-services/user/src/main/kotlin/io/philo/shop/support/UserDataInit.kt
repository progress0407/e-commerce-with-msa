package io.philo.shop.support

import io.philo.shop.domain.entity.UserEntity
import io.philo.shop.domain.repository.UserRepository
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

        val userEntity = UserEntity(
            email = "swcho@naver.com",
            name = "성우",
            address = "노량진",
            rawPassword = "12345678"
        )

        userRepository.save(userEntity)
    }
}