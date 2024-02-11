package io.philo.unit

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.shop.domain.entity.UserEntity

class UserTest : StringSpec({

    "사용자 생성 및 암호화 검증" {
        val userEntity = UserEntity(email = "philo@gmail.com", name = "cho", address = "korea", rawPassword = "1234")

        println("user = ${userEntity}")

        userEntity.isSamePassword("1234") shouldBe true
        userEntity.isSamePassword("9999") shouldBe false
    }
})