package codereview.simpleorder.presentation.command.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.dto.order.OrderLineRequest;
import codereview.simpleorder.dto.order.CreateOrderRequest;
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

    // TODO 요청도 E2E 로
    @Test
    void order는_주문을_하고_주문_ID를_반환한다() {

        // given
        List<Item> initData = JsonFileConverter.fromJsonFile("/init-clothes-data.json", Item.class);
        itemRepository.saveAll(initData);

        List<OrderLineRequest> orderLineRequests = JsonFileConverter.fromJsonFile("/create-order-request.json", OrderLineRequest.class);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderLineRequests);

        // when
        ExtractableResponse<Response> response = post("/order", createOrderRequest);
        Long savedId = response.as(Long.class);

        // then
        assertAll(
                assertEquality(response.statusCode(), HttpStatus.CREATED.value()),
                assertNotNull(savedId)
        );
    }
}
