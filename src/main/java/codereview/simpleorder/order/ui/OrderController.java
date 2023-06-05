package codereview.simpleorder.order.ui;

import codereview.simpleorder.order.application.OrderService;
import codereview.simpleorder.order.dto.web.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long order(@RequestBody CreateOrderRequest request) {

        Long savedOrderId = orderService.order(request);

        return savedOrderId;
    }
}
