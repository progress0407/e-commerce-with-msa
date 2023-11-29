package io.philo.integration.not_success_case

import io.kotest.matchers.shouldBe
import io.philo.presentation.dto.create.UserCreateRequest
import io.philo.presentation.dto.create.UserCreateResponse

class UserAcceptanceTest_Fail() : AcceptanceTest_Fail({

    "사용자 생성 및 암호화 검증" {
        val requestBody = UserCreateRequest(
            email = "jason0101@example.com",
            name = "jason",
            address = "seoul yongsangu",
            password = "1234"
        )

        val response = post(uri = "/users", body = requestBody)

        val userId = response.`as`(UserCreateResponse::class.java).id

        (userId > 0) shouldBe true
    }
})