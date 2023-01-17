package codereview.simpleorder.repository.command;

import codereview.simpleorder.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderItem, Long> {
}

