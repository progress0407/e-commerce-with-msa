package codereview.simpleorder.support;

import codereview.simpleorder.order.application.OrderService;
import codereview.simpleorder.item.repository.ItemRepository;
import codereview.simpleorder.repository.command.OrderLineRepository;
import codereview.simpleorder.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractServiceTest {

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderLineRepository orderLineRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        orderRepository.deleteAll();
    }
}
