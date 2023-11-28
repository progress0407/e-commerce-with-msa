package io.philo.integration

import io.kotest.matchers.shouldBe

class UserAcceptanceTest() : AcceptanceTest({
    "사용자 생성 및 암호화 검증" {
        val requestBody = io.philo.presentation.dto.create.UserCreateRequest(
            email = "jason0101@example.com",
            name = "jason",
            address = "seoul yongsangu",
            password = "1234"
        )

        val response = post(uri = "/users", body = requestBody)

        val userId = response.`as`(io.philo.presentation.dto.create.UserCreateResponse::class.java).id

        println("coroutineContext = ${coroutineContext}")
        println("userId = ${userId}")

        (userId > 0) shouldBe false
    }
})