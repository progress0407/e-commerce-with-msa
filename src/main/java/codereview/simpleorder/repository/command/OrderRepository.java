package codereview.simpleorder.repository.command;

import codereview.simpleorder.domain.item.Clothes;
import codereview.simpleorder.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
