package codereview.simpleorder.acceptance;

import codereview.simpleorder.dto.request.CreateItemRequest;
import codereview.simpleorder.dto.request.CreateOrderRequest;
import codereview.simpleorder.dto.request.OrderLineRequest;
import codereview.simpleorder.support.AbstractAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static codereview.simpleorder.support.TestUtils.assertEquality;
import static codereview.simpleorder.support.TestUtils.assertNotNull;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void 주문을_하면_주문ID를_반환한다() {

        // given
        List<Long> itemIds = createItems();

        // when
        ExtractableResponse<Response> response = post("/orders", createOrderRequest(itemIds));
        Long savedId = extractId(response);

        // then
        assertAll(
                assertEquality(response.statusCode(), HttpStatus.CREATED.value()),
                assertNotNull(savedId)
        );
    }

    private List<Long> createItems() {

        List<CreateItemRequest> itemRequests = createItemRequests();

        List<Long> itemIds = createItemRequest(itemRequests);

        return itemIds;
    }

    private List<Long> createItemRequest(List<CreateItemRequest> itemRequests) {
        List<Long> itemIds = new ArrayList<>();

        for (var request : itemRequests) {
            ExtractableResponse<Response> response = post("/items", request);
            Long id = extractId(response);
            itemIds.add(id);
        }
        return itemIds;
    }

    CreateOrderRequest createOrderRequest(List<Long> itemIds) {

        List<OrderLineRequest> orderLineRequests = itemIds.stream()
                .map(itemId -> new OrderLineRequest(itemId, 1_000))
                .collect(toList());

        return new CreateOrderRequest(orderLineRequests);
    }
}
