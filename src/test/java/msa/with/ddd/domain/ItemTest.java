package msa.with.ddd.domain;

import msa.with.ddd.item.domain.Item;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest {

    @Nested
    class decreaseQuantity_메서드는 {

        @Test
        void 재고가_충분하면_재고를_차감한다() {

            // given
            Item item = new Item("블랙 스웨터", "100L", 55_000, 5);

            // when
            item.decreaseQuantity(3);

            // then
            assertThat(item.getAvailableQuantity()).isEqualTo(2);
        }

        @Test
        void 재고가_충분하지_않으면_예외를_반환한다() {

            // given
            Item item = new Item("블랙 스웨터", "100L", 55_000, 5);

            // when & then
            assertThatThrownBy(
                    () -> item.decreaseQuantity(7)
            ).isInstanceOf(IllegalStateException.class);
        }
    }
}