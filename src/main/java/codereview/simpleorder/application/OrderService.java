package codereview.simpleorder.application;

import codereview.simpleorder.domain.Item;
import codereview.simpleorder.domain.Order;
import codereview.simpleorder.domain.OrderItem;
import codereview.simpleorder.dto.request.CreateOrderRequest;
import codereview.simpleorder.dto.request.OrderLineRequest;
import codereview.simpleorder.repository.ItemRepository;
import codereview.simpleorder.repository.OrderRepository;
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
        validateAndDecreaseItemQuantity(items, orderLineRequests);
        List<OrderItem> orderItems = createOrderLines(items, orderLineRequests);
        Order order = Order.createOrder(orderItems);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    private static List<Long> extractItemIds(List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .map(OrderLineRequest::getItemId)
                .collect(toList());
    }

    private void validateAndDecreaseItemQuantity(List<Item> items, List<OrderLineRequest> orderLineRequests) {

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

    private List<OrderItem> createOrderLines(List<Item> items, List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .map(request -> createOrderLine(items, request))
                .collect(toList());
    }

    private OrderItem createOrderLine(List<Item> items, OrderLineRequest request) {

        Item item = findItemFromDto(items, request);

        return new OrderItem(item.getId(), item.getName(), item.getSize(), item.getPrice(), request.getQuantity());
    }

    private static Item findItemFromDto(List<Item> items, OrderLineRequest orderLineRequest) {

        return items.stream()
                .filter(it -> it.getId().equals(orderLineRequest.getItemId()))
                .findAny()
                .get();
    }
}
