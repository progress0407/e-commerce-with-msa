package msa.with.ddd.acceptance;

import msa.with.ddd.item.dto.web.CreateItemRequest;
import msa.with.ddd.item.dto.web.ItemResponse;
import msa.with.ddd.item.dto.web.ItemResponses;
import msa.with.ddd.support.AbstractAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import msa.with.ddd.support.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

class ItemQueryAcceptanceTest extends AbstractAcceptanceTest {


    @Test
    void item_조회는_리스트를_반환한다() {

        // given
        List<CreateItemRequest> itemRequests = createItemRequests();
        for (var request : itemRequests) {
            post("/items", request);
        }

        // when
        ExtractableResponse<Response> response = get("/items");
        ItemResponses 조회한_리스트 = response.as(ItemResponses.class);
        ItemResponse 양털_스웨터 = findOneBy(조회한_리스트, "양털 스웨터");
        ItemResponse 기모_블랙진 = findOneBy(조회한_리스트, "기모 블랙진");

        // then
        Assertions.assertAll(
                TestUtils.assertEquality(response.statusCode(), HttpStatus.OK.value()),

                TestUtils.assertEquality(양털_스웨터.getSize(), "100L"),

                TestUtils.assertNotNull(양털_스웨터.getId()),
                TestUtils.assertEquality(양털_스웨터.getSize(), "100L"),
                TestUtils.assertEquality(양털_스웨터.getPrice(), 20_000),
                TestUtils.assertEquality(양털_스웨터.getAvailableQuantity(), 1_000),

                TestUtils.assertNotNull(기모_블랙진.getId()),
                TestUtils.assertEquality(기모_블랙진.getSize(), "95M"),
                TestUtils.assertEquality(기모_블랙진.getPrice(), 55000),
                TestUtils.assertEquality(기모_블랙진.getAvailableQuantity(), 2_000)
        );
    }

    private static ItemResponse findOneBy(ItemResponses itemResponses, String name) {

        return itemResponses.getItems().stream()
                .filter(itemResponse -> itemResponse.getName().equals(name))
                .findAny()
                .get();
    }
}
