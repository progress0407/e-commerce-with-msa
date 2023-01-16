package codereview.simpleorder.application.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.domain.order.Order;
import codereview.simpleorder.domain.order.OrderLine;
import codereview.simpleorder.dto.order.CreateOrderLineRequest;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import codereview.simpleorder.repository.command.ItemRepository;
import codereview.simpleorder.repository.command.OrderLineRepository;
import codereview.simpleorder.repository.command.OrderRepository;
import codereview.simpleorder.support.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static codereview.simpleorder.support.TestUtils.assertEquality;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Test
    void order() throws InterruptedException {
        // given
        Item 블랙_스웨터 = itemRepository.save(new Item("블랙 스웨터", "100L", 55_000, 10));
        Item 스니키_청바지 = itemRepository.save(new Item("스니키 청바지", "95M", 35_000, 10));

        Long 블랙_스웨터_ID = 블랙_스웨터.getId();
        Long 스니키_청바지_ID = 스니키_청바지.getId();

        var orderLineRequests =
                List.of(new CreateOrderLineRequest(블랙_스웨터_ID, "100L", 5),
                        new CreateOrderLineRequest(스니키_청바지_ID, "100L", 8));
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderLineRequests);

        // when
        orderService.order(createOrderRequest);

        // then
        int 블랙_스웨터_재고수량 = itemRepository.findById(블랙_스웨터_ID).get().getAvailableQuantity();
        int 스니키_청바지_재고수량 = itemRepository.findById(스니키_청바지_ID).get().getAvailableQuantity();
        List<Order> 주문들 = orderRepository.findAll();
        List<OrderLine> 주문항목들 = orderLineRepository.findAll();

        assertAll(
                assertEquality(블랙_스웨터_재고수량, 10 - 5),
                assertEquality(스니키_청바지_재고수량, 10 - 8),
                assertEquality(주문들.size(), 1),
                assertEquality(주문항목들.size(), 2)
        );
    }
}
