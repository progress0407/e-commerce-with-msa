package codereview.simpleorder.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractAcceptanceTest {

    @LocalServerPort
    protected int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> get(String uri) {
        return RestAssured.given().log().all()
                .accept(APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all().extract();
    }

    protected <T> Executable assertEquality(T actual, T expected) {
        return () -> assertThat(actual).isEqualTo(expected);
    }

    protected <T> Executable assertNotNull(T actual) {
        return () -> assertThat(actual).isNotNull();
    }
}
