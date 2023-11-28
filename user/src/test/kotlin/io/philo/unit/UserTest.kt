package io.philo.unit

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.domain.entity.User

class UserTest : StringSpec({

    "사용자 생성 및 암호화 검증" {
        val user = User(email = "philo@gmail.com", name = "cho", address = "korea", rawPassword = "1234")

        println("user = ${user}")

        user.isSamePassword("1234") shouldBe true
        user.isSamePassword("9999") shouldBe false
    }
})