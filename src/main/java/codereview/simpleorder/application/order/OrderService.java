package codereview.simpleorder.application.order;

import codereview.simpleorder.domain.item.Clothes;
import codereview.simpleorder.domain.order.Order;
import codereview.simpleorder.domain.order.OrderLine;
import codereview.simpleorder.dto.item.CreateOrderLineRequest;
import codereview.simpleorder.dto.item.CreateOrderRequest;
import codereview.simpleorder.repository.command.ClothesRepository;
import codereview.simpleorder.repository.command.OrderLineRepository;
import codereview.simpleorder.repository.command.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ClothesRepository clothesRepository;

    public Long order(CreateOrderRequest request) {

        List<CreateOrderLineRequest> orderLineRequests = request.getCreateOrderLineRequests();

        List<OrderLine> orderLines = createOrder(orderLineRequests);

        Order order = Order.create(orderLines);

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    private List<OrderLine> createOrder(List<CreateOrderLineRequest> createOrderLineRequests) {
        List<OrderLine> orderLines = new ArrayList<>();
        for (var orderLineRequest : createOrderLineRequests) {
            Clothes clothes = clothesRepository.findById(orderLineRequest.getItemId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLine orderLine = orderLineRequest.toEntity(clothes);
            orderLines.add(orderLine);
        }
        return orderLines;
    }
}
