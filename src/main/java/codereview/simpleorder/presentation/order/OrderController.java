package codereview.simpleorder.presentation.order;

import codereview.simpleorder.application.order.OrderService;
import codereview.simpleorder.dto.order.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
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
