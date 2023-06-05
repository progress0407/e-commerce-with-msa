package codereview.simpleorder.order.dto.event;

import codereview.simpleorder.order.domain.OrderItem;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class OrderCreatedEvent {

    private final Map<Long, Integer> itemIdToDecreaseQuantity;

    public OrderCreatedEvent(List<OrderItem> orderItems) {

        this.itemIdToDecreaseQuantity = createInnerDto(orderItems);
    }

    private static Map<Long, Integer> createInnerDto(List<OrderItem> orderItems) {

        return orderItems.stream()
                .collect(toMap(OrderItem::getItemId, OrderItem::getOrderItemQuantity));
    }

    public Map<Long, Integer> values() {

        return itemIdToDecreaseQuantity;
    }
}
