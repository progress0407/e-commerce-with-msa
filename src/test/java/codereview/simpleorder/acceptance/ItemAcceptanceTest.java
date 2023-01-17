package codereview.simpleorder.acceptance;

import codereview.simpleorder.dto.item.CreateItemRequest;
import codereview.simpleorder.support.AbstractAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static codereview.simpleorder.support.TestUtils.assertEquality;
import static codereview.simpleorder.support.TestUtils.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;

class ItemAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void 상품등록을_하면_상품ID를_반환한다() {

        // given
        CreateItemRequest itemRequest = itemRequests().get(0);

        // when
        ExtractableResponse<Response> response = post("/items", itemRequest);
        Long savedId = extractId(response);

        // then
        assertAll(
                assertEquality(response.statusCode(), HttpStatus.CREATED.value()),
                assertNotNull(savedId)
        );
    }
}