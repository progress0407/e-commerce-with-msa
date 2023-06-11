package msa.with.ddd.repository.command;

import msa.with.ddd.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderItem, Long> {
}

