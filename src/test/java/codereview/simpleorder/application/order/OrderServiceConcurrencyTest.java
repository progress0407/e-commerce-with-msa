package codereview.simpleorder.application.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.dto.order.CreateOrderLineRequest;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import codereview.simpleorder.repository.command.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class OrderServiceConcurrencyTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ItemRepository itemRepository;

    static final int TRIALS_NUMBER = 100;
    ExecutorService threadPool = Executors.newFixedThreadPool(TRIALS_NUMBER);
    CountDownLatch latch = new CountDownLatch(TRIALS_NUMBER);
    int OptimisticLockExceptionCount = 0;

    @Test
    void order() throws InterruptedException {
        // given
        Item savedItem = itemRepository.save(new Item("블랙 스웨터", "100L", 55_000, 100));
        Long savedId = savedItem.getId();

        var orderLineRequests = List.of(new CreateOrderLineRequest(savedId, "100L", 1));
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderLineRequests);

        // when
        for (int i = 0; i < TRIALS_NUMBER; i++) {
            threadPool.execute(order(latch, createOrderRequest));
            System.out.println("trialsNumber = " + TRIALS_NUMBER);
        }

        latch.await();
        threadPool.shutdown();

        // then
        Item foundItem = itemRepository.findById(savedId).get();
        System.out.println("foundItem = " + foundItem);
        System.out.println("OptimisticLockExceptionCount = " + OptimisticLockExceptionCount);
    }

    private Runnable order(CountDownLatch latch, CreateOrderRequest createOrderRequest) {
        return () -> {
            latch.countDown();
            try {
                orderService.order(createOrderRequest);
            } catch (ObjectOptimisticLockingFailureException exception) {
                OptimisticLockExceptionCount++;
            }
        };
    }
}
