package codereview.simpleorder.application.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.domain.order.Order;
import codereview.simpleorder.domain.order.OrderLine;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import codereview.simpleorder.dto.order.OrderLineRequest;
import codereview.simpleorder.repository.command.ItemRepository;
import codereview.simpleorder.repository.command.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(CreateOrderRequest orderRequest) {

        List<OrderLineRequest> orderLineRequests = orderRequest.getOrderLineRequests();
        List<Long> itemIds = extractItemIds(orderLineRequests);
        List<Item> items = itemRepository.findByIdIn(itemIds);
        decreaseItemQuantity(items, orderLineRequests);
        List<OrderLine> orderLines = createOrderLines(items, orderLineRequests);
        Order order = Order.createOrder(orderLines);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    private static List<Long> extractItemIds(List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .map(OrderLineRequest::getItemId)
                .collect(toList());
    }

    private void decreaseItemQuantity(List<Item> items, List<OrderLineRequest> orderLineRequests) {

        for (Item item : items) {
            int orderQuantity = findOrderQuantity(item, orderLineRequests);
            item.decreaseQuantity(orderQuantity);
        }
    }

    private static int findOrderQuantity(Item item, List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .filter(request -> request.getItemId().equals(item.getId()))
                .mapToInt(OrderLineRequest::getQuantity)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLine> createOrderLines(List<Item> items, List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .map(request -> createOrderLine(items, request))
                .collect(toList());
    }

    private OrderLine createOrderLine(List<Item> items, OrderLineRequest request) {

        Item item = findItem(items, request);

        return new OrderLine(item.getId(), item.getName(), item.getSize(), item.getPrice(), request.getQuantity());
    }

    private static Item findItem(List<Item> items, OrderLineRequest orderLineRequest) {

        return items.stream()
                .filter(it -> it.getId().equals(orderLineRequest.getItemId()))
                .findAny()
                .get();
    }
}
