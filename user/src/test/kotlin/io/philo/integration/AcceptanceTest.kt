package io.philo.integration

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.spec.style.StringSpecTestFactoryConfiguration
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import javax.sql.DataSource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
open class AcceptanceTest(block: StringSpecTestFactoryConfiguration.() -> Unit):StringSpec( {
}) {
    @LocalServerPort
    var port = 0

    @PersistenceContext
    var entityManager: EntityManager? = null

    @Autowired
    var dataSource: DataSource? = null

    @BeforeEach
    protected fun setUp() {
        RestAssured.port = port
    }

    companion object {
        fun post(uri: String?, body: Any?): ExtractableResponse<Response> {
            return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post(uri)
                .then().log().all()
                .extract()
        }
    }

    protected fun post(uri: String?, body: Any?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`().post(uri)
            .then().log().all()
            .extract()
    }

    protected fun post(uri: String?, body: Any?, token: String?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`().post(uri)
            .then().log().all()
            .extract()
    }

    protected fun get(uri: String?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`()[uri]
            .then().log().all()
            .extract()
    }

    protected fun get(uri: String?, token: String?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`()[uri]
            .then().log().all()
            .extract()
    }

    protected fun put(uri: String?, requestBody: Any?, token: String?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .body(requestBody)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`().put(uri)
            .then().log().all()
            .extract()
    }

    protected fun delete(uri: String?, token: String?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`().delete(uri)
            .then().log().all()
            .extract()
    }

    protected fun delete(uri: String?, requestBody: Any?, token: String?): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .body(requestBody)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`().delete(uri)
            .then().log().all()
            .extract()
    }
}