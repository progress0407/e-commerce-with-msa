package io.philo.integration

import io.kotest.matchers.shouldBe
import io.philo.presentation.dto.create.UserCreateRequest
import io.philo.presentation.dto.create.UserCreateResponse
import org.junit.jupiter.api.Test

class UserAcceptanceTest : AcceptanceTest() {

    @Test
    fun `회원가입한다`() {

        val requestBody = UserCreateRequest.fixture()

        val userId = postAndGetBody<UserCreateResponse>(uri = "/users", body = requestBody).id

        (userId > 0) shouldBe true
    }

    fun UserCreateRequest.Companion.fixture(): UserCreateRequest = UserCreateRequest(
        email = "jason0101@example.com",
        name = "jason",
        address = "seoul yongsangu",
        password = "1234"
    )
}