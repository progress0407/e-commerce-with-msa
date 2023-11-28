package io.philo.integration

import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import javax.sql.DataSource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAcceptanceTest2 {

    @LocalServerPort
    var port = 0

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var dataSource: DataSource

    @BeforeEach
    protected fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun `aa`() {

        val requestBody = io.philo.presentation.dto.create.UserCreateRequest(
            email = "jason0101@example.com",
            name = "jason",
            address = "seoul yongsangu",
            password = "1234"
        )

        val extractableResponse = AcceptanceTest.post(uri = "/users", body = requestBody)

        println("extractableResponse = ${extractableResponse}")

        val userId = extractableResponse.`as`(io.philo.presentation.dto.create.UserCreateResponse::class.java).id

        println("userId = ${userId}")

        (userId > 0) shouldBe false
    }
}