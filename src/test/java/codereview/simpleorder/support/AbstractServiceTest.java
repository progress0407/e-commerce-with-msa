package codereview.simpleorder.support;

import codereview.simpleorder.application.OrderService;
import codereview.simpleorder.repository.command.ItemRepository;
import codereview.simpleorder.repository.command.OrderLineRepository;
import codereview.simpleorder.repository.command.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
