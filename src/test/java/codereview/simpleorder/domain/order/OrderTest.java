package codereview.simpleorder.domain.order;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void create_메서드는_각_주문항목의_총합계를_계산한다() {
        // given
        OrderLine 블랙_스웨터 = new OrderLine(1L, "블랙 스웨터", "100L", 40_000, 1);
        OrderLine 베이지_니트 = new OrderLine(2L, "베이지 니트", "95M", 30_000, 2);

        // when
        Order order = Order.createOrder(List.of(블랙_스웨터, 베이지_니트));

        // then
        assertThat(order.getTotalOrderAmount()).isEqualTo(100_000);
    }
}