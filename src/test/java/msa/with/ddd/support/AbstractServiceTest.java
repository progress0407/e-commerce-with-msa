package msa.with.ddd.support;

import msa.with.ddd.item.repository.ItemRepository;
import msa.with.ddd.order.application.OrderService;
import msa.with.ddd.repository.command.OrderLineRepository;
import msa.with.ddd.order.repository.OrderRepository;
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
