package msa.with.ddd.acceptance;

import msa.with.ddd.item.dto.item.CreateItemRequest;
import msa.with.ddd.order.dto.web.CreateOrderRequest;
import msa.with.ddd.order.dto.web.OrderLineRequest;
import msa.with.ddd.support.AbstractAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import msa.with.ddd.support.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

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
        Assertions.assertAll(
                TestUtils.assertEquality(response.statusCode(), HttpStatus.CREATED.value()),
                TestUtils.assertNotNull(savedId)
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
