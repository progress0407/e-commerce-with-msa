package codereview.simpleorder.support;

import codereview.simpleorder.item.dto.web.CreateItemRequest;
import codereview.simpleorder.item.repository.ItemRepository;
import codereview.simpleorder.order.repository.OrderRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractAcceptanceTest {

//    @LocalServerPort
//    protected int port;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @BeforeEach
    void setUp() {

//        RestAssured.port = port;

        itemRepository.deleteAll();
        orderRepository.deleteAll();
    }

    protected ExtractableResponse<Response> get(String uri) {

        return RestAssured.given().log().all()
                .accept(APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all().extract();
    }

    protected <T> ExtractableResponse<Response> post(String uri, T requestBody) {

        return RestAssured.given().log().all()
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(uri)
                .then().log().all().extract();
    }

    protected static Long extractId(ExtractableResponse<Response> response) {

        return response.as(Long.class);
    }

    protected static List<CreateItemRequest> createItemRequests() {

        return JsonFileConverter.fromJsonFile("/init-item.json", CreateItemRequest.class);
    }
}
