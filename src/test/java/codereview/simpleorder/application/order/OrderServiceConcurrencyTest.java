package codereview.simpleorder.application.order;

import codereview.simpleorder.item.domain.Item;
import codereview.simpleorder.order.dto.web.CreateOrderRequest;
import codereview.simpleorder.order.dto.web.OrderLineRequest;
import codereview.simpleorder.support.AbstractServiceTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceConcurrencyTest extends AbstractServiceTest {

    static final int 총_요청횟수 = 100;
    static final int 초기_재고수량 = 총_요청횟수;
    ExecutorService threadPool = Executors.newFixedThreadPool(총_요청횟수);
    CountDownLatch latch = new CountDownLatch(총_요청횟수);
    AtomicInteger optimisticLockExceptionCount = new AtomicInteger();

    // TODO Resolve Dead Lock
    @Disabled
    @Test
    void 주문에_대한_동시성에_대해_안전한지_검증한다() throws InterruptedException {

        // given
        Item savedItem = itemRepository.save(new Item("블랙 스웨터", "100L", 55_000, 초기_재고수량));
        Long savedId = savedItem.getId();

        var orderLineRequests = List.of(new OrderLineRequest(savedId, 1));
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderLineRequests);

        // when
        for (int i = 0; i < 총_요청횟수; i++) {
            threadPool.execute(order(latch, createOrderRequest));
        }

        latch.await();
        threadPool.shutdown();

        // then
        int 남은_재고량 = itemRepository.findById(savedId).get().getAvailableQuantity();
        int 정상_주문량 = 초기_재고수량 - 남은_재고량;
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
