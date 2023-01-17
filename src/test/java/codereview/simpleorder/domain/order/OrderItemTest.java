package codereview.simpleorder.domain.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    void orderLineAmount_메서드는_주문항목_금액을_반환한다() {

        // given
        OrderItem orderItem = new OrderItem(1L, "블랙 스웨터", "100L", 10_000, 5);

        // when
        int orderLineAmount = orderItem.orderLineAmount();

        // then
        assertThat(orderLineAmount).isEqualTo(50_000);
    }
}