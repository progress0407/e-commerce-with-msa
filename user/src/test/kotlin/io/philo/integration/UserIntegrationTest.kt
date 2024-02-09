package io.philo.integration

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.philo.shop.AcceptanceTest
import io.philo.shop.domain.entity.UserEntity
import io.philo.shop.presentation.dto.create.UserCreateRequestDto
import io.philo.shop.presentation.dto.create.UserCreateResponseDto
import io.philo.shop.presentation.dto.login.UserLoginRequest
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.AUTHORIZATION

class UserIntegrationTest : AcceptanceTest() {

    @Test
    fun `회원가입 후 로그인 한다`() {

        // given
        val requestBody = UserCreateRequestDto.fixture

        // when
        val userId = 회원가입(requestBody).id
        val loginResponse = 로그인()

        // then
        val accessToken = loginResponse.authHeader

        (userId > 0) shouldBe true // id 생성 검증
        accessToken shouldNotBe null // token 존재 검증
    }

    private fun 회원가입(requestBody: UserCreateRequestDto) =
        postAndGetBody<UserCreateResponseDto>("/users", requestBody)

    private fun 로그인() = post("/users/login", UserLoginRequest.fixture)

    val ExtractableResponse<Response>.authHeader
        get() = this.header(AUTHORIZATION)

    val UserCreateRequestDto.Companion.fixture: UserCreateRequestDto
        get() = UserCreateRequestDto(
            email = UserEntity.fixture.email,
            name = UserEntity.fixture.name,
            address = UserEntity.fixture.address,
            password = UserEntity.fixture_rawPassword
        )

    val UserEntity.Companion.fixture: UserEntity
        get() = UserEntity(
            email = "jason0101@example.com",
            name = "jason",
            address = "seoul yongsangu",
            rawPassword = UserEntity.fixture_rawPassword
        )

    val UserEntity.Companion.fixture_rawPassword
        get() = "1234"

    val UserLoginRequest.Companion.fixture: Any
        get() = UserLoginRequest(email = UserEntity.fixture.email, password = UserEntity.fixture_rawPassword)
}
