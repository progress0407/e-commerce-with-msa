package codereview.simpleorder.order.repository;

import codereview.simpleorder.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
