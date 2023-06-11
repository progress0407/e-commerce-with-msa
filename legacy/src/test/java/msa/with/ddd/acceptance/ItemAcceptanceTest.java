package msa.with.ddd.acceptance;

import msa.with.ddd.item.dto.web.CreateItemRequest;
import msa.with.ddd.support.AbstractAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import msa.with.ddd.support.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertAll;

class ItemAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void 상품등록을_하면_상품ID를_반환한다() {

        // given
        CreateItemRequest itemRequest = createItemRequests().get(0);

        // when
        ExtractableResponse<Response> response = post("/items", itemRequest);
        Long savedId = extractId(response);

        // then
        Assertions.assertAll(
                TestUtils.assertEquality(response.statusCode(), HttpStatus.CREATED.value()),
                TestUtils.assertNotNull(savedId)
        );
    }
}