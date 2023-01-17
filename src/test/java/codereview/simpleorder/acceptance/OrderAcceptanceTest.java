package codereview.simpleorder.acceptance;

import codereview.simpleorder.dto.item.CreateItemRequest;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import codereview.simpleorder.dto.order.OrderLineRequest;
import codereview.simpleorder.support.AbstractAcceptanceTest;
import codereview.simpleorder.support.JsonFileConverter;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static codereview.simpleorder.support.TestUtils.assertEquality;
import static codereview.simpleorder.support.TestUtils.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void 주문을_하면_주문ID를_반환한다() {

        // given
        List<CreateItemRequest> itemRequests = itemRequests();
        for (var request : itemRequests) {
            post("/items", request);
        }

        CreateOrderRequest createOrderRequest = createOrderRequest();

        // when
        ExtractableResponse<Response> response = post("/orders", createOrderRequest);
        Long savedId = extractId(response);

        // then
        assertAll(
                assertEquality(response.statusCode(), HttpStatus.CREATED.value()),
                assertNotNull(savedId)
        );
    }

    CreateOrderRequest createOrderRequest() {

        List<OrderLineRequest> orderLineRequests = JsonFileConverter.fromJsonFile("/create-order-request.json", OrderLineRequest.class);
        return new CreateOrderRequest(orderLineRequests);
    }
}
