package codereview.simpleorder.application.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.dto.order.OrderLineRequest;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import codereview.simpleorder.repository.command.ItemRepository;
import codereview.simpleorder.repository.command.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceConcurrencyTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    static final int 총_요청횟수 = 100;
    ExecutorService threadPool = Executors.newFixedThreadPool(총_요청횟수);
    CountDownLatch latch = new CountDownLatch(총_요청횟수);
    AtomicInteger optimisticLockExceptionCount = new AtomicInteger();

    @BeforeEach
    void setUp() {

        itemRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void 주문에_대한_동시성에_대해_안전한지_검증한다() throws InterruptedException {

        // given
        Item savedItem = itemRepository.save(new Item("블랙 스웨터", "100L", 55_000, 100));
        Long savedId = savedItem.getId();

        var orderLineRequests = List.of(new OrderLineRequest(savedId, 1));
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderLineRequests);

        // when
        for (int i = 0; i < 총_요청횟수; i++) {
            threadPool.execute(order(latch, createOrderRequest));
            System.out.println("trialsNumber = " + 총_요청횟수);
        }

        latch.await();
        threadPool.shutdown();

        // then
        int 남은_재고량 = itemRepository.findById(savedId).get().getAvailableQuantity();
        int 정상_주문량 = 총_요청횟수 - 남은_재고량;
        int 실패_주문량 = optimisticLockExceptionCount.get();

        assertThat(정상_주문량 + 실패_주문량).isEqualTo(총_요청횟수);
    }

    private Runnable order(CountDownLatch latch, CreateOrderRequest createOrderRequest) {
        return () -> {
            latch.countDown();
            try {
                orderService.order(createOrderRequest);
            } catch (ObjectOptimisticLockingFailureException exception) {
                optimisticLockExceptionCount.incrementAndGet();
            }
        };
    }
}
