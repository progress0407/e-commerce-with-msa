package codereview.simpleorder.order.application;

import codereview.simpleorder.order.domain.Order;
import codereview.simpleorder.order.domain.OrderItem;
import codereview.simpleorder.order.dto.web.CreateOrderRequest;
import codereview.simpleorder.order.dto.web.ItemResponse;
import codereview.simpleorder.order.dto.web.OrderLineRequest;
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

        List<OrderItem> orderItems = createOrderLines(itemResponses, orderLineRequests);
        Order order = Order.createOrder(orderItems);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    private static List<Long> extractItemIds(List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .map(OrderLineRequest::getItemId)
                .collect(toList());
    }

    private List<OrderItem> createOrderLines(List<ItemResponse> itemResponses, List<OrderLineRequest> orderLineRequests) {

        return orderLineRequests.stream()
                .map(request -> createOrderLine(itemResponses, request))
                .collect(toList());
    }

    private OrderItem createOrderLine(List<ItemResponse> itemResponses, OrderLineRequest orderLineRequest) {

        ItemResponse itemResponse = findItemDtoFromOrderLineRequest(itemResponses, orderLineRequest);

        return new OrderItem(
                itemResponse.getId(),
                itemResponse.getName(),
                itemResponse.getSize(),
                itemResponse.getPrice(),
                orderLineRequest.getQuantity());
    }

    private static ItemResponse findItemDtoFromOrderLineRequest(List<ItemResponse> itemResponses, OrderLineRequest orderLineRequest) {

        return itemResponses.stream()
                .filter(it -> it.getId().equals(orderLineRequest.getItemId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("주문 항목 요청에 해당하는 상품이 없습니다."));
    }
}
