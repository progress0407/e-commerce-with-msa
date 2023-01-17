package codereview.simpleorder.acceptance;

import codereview.simpleorder.dto.item.CreateItemRequest;
import codereview.simpleorder.dto.item.ItemResponse;
import codereview.simpleorder.dto.item.ItemResponses;
import codereview.simpleorder.support.AbstractAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static codereview.simpleorder.support.TestUtils.assertEquality;
import static codereview.simpleorder.support.TestUtils.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;

class ItemQueryAcceptanceTest extends AbstractAcceptanceTest {


    @Test
    void clothes_조회는_리스트를_반환한다() {

        // given
        List<CreateItemRequest> itemRequests = itemRequests();
        for (var request : itemRequests) {
            post("/items", request);
        }

        // when
        ExtractableResponse<Response> response = get("/items");
        ItemResponses 조회한_리스트 = response.as(ItemResponses.class);
        ItemResponse 양털_스웨터 = findOneBy(조회한_리스트, "양털 스웨터");
        ItemResponse 기모_블랙진 = findOneBy(조회한_리스트, "기모 블랙진");

        // then
        assertAll(
                assertEquality(response.statusCode(), HttpStatus.OK.value()),

                assertEquality(양털_스웨터.getSize(), "100L"),

                assertNotNull(양털_스웨터.getId()),
                assertEquality(양털_스웨터.getSize(), "100L"),
                assertEquality(양털_스웨터.getPrice(), 20_000),
                assertEquality(양털_스웨터.getAvailableQuantity(), 1_000),

                assertNotNull(기모_블랙진.getId()),
                assertEquality(기모_블랙진.getSize(), "95M"),
                assertEquality(기모_블랙진.getPrice(), 55000),
                assertEquality(기모_블랙진.getAvailableQuantity(), 2_000)
        );
    }

    private static ItemResponse findOneBy(ItemResponses itemResponses, String name) {

        return itemResponses.getClothes().stream()
                .filter(itemResponse -> itemResponse.getName().equals(name))
                .findAny()
                .get();
    }
}
