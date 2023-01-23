package codereview.simpleorder.order.application;

import codereview.simpleorder.item.domain.Item;
import codereview.simpleorder.order.dto.CreateOrderRequest;
import codereview.simpleorder.order.dto.ItemResponse;
import codereview.simpleorder.order.dto.OrderLineRequest;
import codereview.simpleorder.order.domain.Order;
import codereview.simpleorder.order.domain.OrderItem;
import codereview.simpleorder.order.repository.OrderRepository;
import codereview.simpleorder.order.rest.ItemFeignClient;
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
    private final ItemFeignClient itemHttpClient;

    @Transactional
    public Long order(CreateOrderRequest orderRequest) {

        List<OrderLineRequest> orderLineRequests = orderRequest.getOrderLineRequests();
        List<Long> itemIds = extractItemIds(orderLineRequests);

        List<ItemResponse> itemResponses = itemHttpClient.requestItems(itemIds).getItems();

//        validateAndDecreaseItemQuantity(items, orderLineRequests); -> Item에서 하기

        List<OrderItem> orderItems = createOrderLines(itemResponses, orderLineRequests); // itemResponses
        Order order = Order.createOrder(orderItems);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    @Transactional
    public void test() {

        itemHttpClient.requestItems(List.of(1L, 2L, 3L));
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

    private List<OrderItem> createOrderLines(List<ItemResponse> itemResponses, List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .map(request -> createOrderLine(itemResponses, request))
                .collect(toList());
    }

    private OrderItem createOrderLine(List<ItemResponse> itemResponses, OrderLineRequest orderLineRequest) {

        ItemResponse itemResponse = findItemFromDto(itemResponses, orderLineRequest);

        return new OrderItem(
                itemResponse.getId(),
                itemResponse.getName(),
                itemResponse.getSize(),
                itemResponse.getPrice(),
                orderLineRequest.getQuantity());
    }

    private static ItemResponse findItemFromDto(List<ItemResponse> itemResponses, OrderLineRequest orderLineRequest) {

        return itemResponses.stream()
                .filter(it -> it.getId().equals(orderLineRequest.getItemId()))
                .findAny()
                .get();
    }
}
