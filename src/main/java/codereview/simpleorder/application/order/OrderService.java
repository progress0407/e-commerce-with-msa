package codereview.simpleorder.application.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.domain.order.Order;
import codereview.simpleorder.domain.order.OrderLine;
import codereview.simpleorder.dto.order.CreateOrderLineRequest;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import codereview.simpleorder.repository.command.ItemRepository;
import codereview.simpleorder.repository.command.OrderLineRepository;
import codereview.simpleorder.repository.command.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(CreateOrderRequest orderRequest) {

        List<CreateOrderLineRequest> orderLineRequests = orderRequest.getCreateOrderLineRequests();
        decreaseItemQuantity(orderLineRequests);
        List<OrderLine> orderLines = createOrderLines(orderLineRequests);
        Order order = Order.createOrder(orderLines);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    private void decreaseItemQuantity(List<CreateOrderLineRequest> orderLineRequests) {

        List<Long> itemIds = extractItemIds(orderLineRequests);
        List<Item> items = itemRepository.findByIdIn(itemIds);

        for (Item item : items) {
            int orderQuantity = findAndConvertToOrderQuantity(orderLineRequests, item);
            item.decreaseQuantity(orderQuantity);
        }
    }

    private static int findAndConvertToOrderQuantity(List<CreateOrderLineRequest> orderLineRequests, Item item) {
        return orderLineRequests.stream()
                .filter(request -> request.getItemId().equals(item.getId()))
                .mapToInt(CreateOrderLineRequest::getQuantity)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static List<Long> extractItemIds(List<CreateOrderLineRequest> orderLineRequests) {
        return orderLineRequests.stream()
                .map(CreateOrderLineRequest::getItemId)
                .collect(toList());
    }

    private List<OrderLine> createOrderLines(List<CreateOrderLineRequest> orderLineRequests) {
        List<OrderLine> orderLines = new ArrayList<>();
        for (var orderLineRequest : orderLineRequests) {
            Item item = itemRepository.findById(orderLineRequest.getItemId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLine orderLine = orderLineRequest.toEntity(item);
            orderLines.add(orderLine);
        }
        return orderLines;
    }
}
