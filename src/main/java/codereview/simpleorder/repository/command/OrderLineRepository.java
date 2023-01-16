package codereview.simpleorder.repository.command;

import codereview.simpleorder.domain.order.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
}
