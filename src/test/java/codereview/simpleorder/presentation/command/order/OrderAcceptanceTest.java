package codereview.simpleorder.presentation.command.order;

import codereview.simpleorder.domain.item.Clothes;
import codereview.simpleorder.dto.item.CreateOrderLineRequest;
import codereview.simpleorder.dto.item.CreateOrderRequest;
import codereview.simpleorder.support.AbstractAcceptanceTest;
import codereview.simpleorder.support.JsonFileConverter;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

class OrderAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void order는_주문을_하고_주문_ID를_반환한다() {
        // given
        List<Clothes> initData = JsonFileConverter.fromJsonFile("/init-clothes-data.json", Clothes.class);
        clothesRepository.saveAll(initData);

        List<CreateOrderLineRequest> orderLineRequests = JsonFileConverter.fromJsonFile("/create-order-request.json", CreateOrderLineRequest.class);
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
