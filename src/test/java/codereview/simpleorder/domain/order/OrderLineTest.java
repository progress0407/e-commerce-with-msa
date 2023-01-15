package codereview.simpleorder.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderLineTest {

    @Test
    void orderLineAmount_메서드는_주문항목_금액을_반환한다() {
        // given
        OrderLine orderLine = new OrderLine(1L, "블랙 스웨터", "100L", 10_000, 5);

        // when
        int orderLineAmount = orderLine.orderLineAmount();

        // then
        assertThat(orderLineAmount).isEqualTo(50_000);
    }
}