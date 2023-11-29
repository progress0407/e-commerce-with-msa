package io.philo.integration

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import javax.sql.DataSource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTest {

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

    protected fun post(uri: String, body: Any): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .body(body)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .`when`().post(uri)
            .then().log().all()
            .extract()
    }

    protected fun post(uri: String, body: Any, token: String): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .body(body)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .`when`().post(uri)
            .then().log().all()
            .extract()
    }


    protected final inline fun <reified T: Any> postAndGetBody(uri: String, body: Any): T {

        val response = RestAssured.given().log().all()
            .body(body)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .`when`().post(uri)
            .then().log().all()
            .extract()
            .`as`(T::class.java)

        return response
    }

    protected fun get(uri: String): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .accept(APPLICATION_JSON_VALUE)
            .`when`()[uri]
            .then().log().all()
            .extract()
    }

    protected fun get(uri: String, token: String): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .accept(APPLICATION_JSON_VALUE)
            .`when`()[uri]
            .then().log().all()
            .extract()
    }

    protected fun put(uri: String, requestBody: Any, token: String): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .body(requestBody)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .`when`().put(uri)
            .then().log().all()
            .extract()
    }

    protected fun delete(uri: String, token: String): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .`when`().delete(uri)
            .then().log().all()
            .extract()
    }

    protected fun delete(uri: String, requestBody: Any, token: String): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .body(requestBody)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE)
            .`when`().delete(uri)
            .then().log().all()
            .extract()
    }
}