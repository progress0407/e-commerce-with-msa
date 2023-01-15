package codereview.simpleorder.presentation;

import codereview.simpleorder.dto.item.ClothesResponses;
import codereview.simpleorder.support.AbstractAcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    void item_메서드() {
        // given

        // when
        ClothesResponses clothesResponses = get("/items").as(ClothesResponses.class);

        // then
        assertThat(clothesResponses).isNotNull();
    }
}
