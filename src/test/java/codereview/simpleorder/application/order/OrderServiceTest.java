package codereview.simpleorder.application.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.domain.order.Order;
import codereview.simpleorder.domain.order.OrderItem;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import codereview.simpleorder.dto.order.OrderLineRequest;
import codereview.simpleorder.support.AbstractServiceTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static codereview.simpleorder.support.TestUtils.assertEquality;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends AbstractServiceTest {

    @Test
    void 주문이_정상적으로_되면_재고수량이_차감된다() {

        // given
        Item 블랙_스웨터 = itemRepository.save(new Item("블랙 스웨터", "100L", 55_000, 10));
        Item 스니키_청바지 = itemRepository.save(new Item("스니키 청바지", "95M", 35_000, 10));

        Long 블랙_스웨터_ID = 블랙_스웨터.getId();
        Long 스니키_청바지_ID = 스니키_청바지.getId();

        var orderLineRequests =
                List.of(new OrderLineRequest(블랙_스웨터_ID, 5),
                        new OrderLineRequest(스니키_청바지_ID, 8));
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderLineRequests);

        // when
        orderService.order(createOrderRequest);

        // then
        int 블랙_스웨터_재고수량 = itemRepository.findById(블랙_스웨터_ID).get().getAvailableQuantity();
        int 스니키_청바지_재고수량 = itemRepository.findById(스니키_청바지_ID).get().getAvailableQuantity();
        List<Order> 주문들 = orderRepository.findAll();
        List<OrderItem> 주문항목들 = orderLineRepository.findAll();

        assertAll(
                assertEquality(블랙_스웨터_재고수량, 10 - 5),
                assertEquality(스니키_청바지_재고수량, 10 - 8),
                assertEquality(주문들.size(), 1),
                assertEquality(주문항목들.size(), 2)
        );
    }
}
