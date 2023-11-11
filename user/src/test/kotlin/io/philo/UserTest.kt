package io.philo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.domain.entity.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserTest : StringSpec({

    "사용자 생성 및 암호화 검증" {
        val passwordEncoder = BCryptPasswordEncoder()
//        val user = User(email = "philo@gmail.com", name="cho", address="korea", rawPassword="1234")
        val user = User("philo@gmail.com", "cho", "korea", "1234")

        user.isSamePassword("1234") shouldBe true
        user.isSamePassword("9999") shouldBe false
    }
})