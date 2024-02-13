package io.philo.shop.support

import io.philo.shop.domain.entity.UserEntity
import io.philo.shop.domain.repository.UserRepository
import jakarta.annotation.PostConstruct

//@Component
class UserDataInitializer(private val userRepository: UserRepository) {

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