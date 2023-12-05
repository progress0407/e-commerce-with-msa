package io.philo.integration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.philo.domain.entity.User
import io.philo.presentation.dto.create.UserCreateRequest
import io.philo.presentation.dto.create.UserCreateResponse
import io.philo.presentation.dto.login.UserLoginRequest
import io.philo.shop.AcceptanceTest
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.AUTHORIZATION

class UserAcceptanceTest : AcceptanceTest() {

    @Test
    fun `회원가입 후 로그인 한다`() {

        val requestBody = UserCreateRequest.fixture

        val userId = postAndGetBody<UserCreateResponse>("/users", requestBody).id

        val loginResponse = post("/users/login", UserLoginRequest.fixture)
        val accessToken = loginResponse.authHeader

        (userId > 0) shouldBe true // id 생성 검증
        accessToken shouldNotBe null // token 존재 검증
    }

    val ExtractableResponse<Response>.authHeader
        get() = this.header(AUTHORIZATION)

    val UserCreateRequest.Companion.fixture: UserCreateRequest
        get() = UserCreateRequest(
            email = User.fixture.email,
            name = User.fixture.name,
            address = User.fixture.address,
            password = User.fixture_rawPassword
        )

    val User.Companion.fixture: User
        get() = User(
            email = "jason0101@example.com",
            name = "jason",
            address = "seoul yongsangu",
            rawPassword = User.fixture_rawPassword
        )

    val User.Companion.fixture_rawPassword
        get() = "1234"

    val UserLoginRequest.Companion.fixture: Any
        get() = UserLoginRequest(email = User.fixture.email, password = User.fixture_rawPassword)
}
