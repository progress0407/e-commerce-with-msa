package codereview.simpleorder.presentation.command.order;

import codereview.simpleorder.application.OrderService;
import codereview.simpleorder.dto.item.CreateOrderRequest;
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
        return orderService.order(request);
    }
}
