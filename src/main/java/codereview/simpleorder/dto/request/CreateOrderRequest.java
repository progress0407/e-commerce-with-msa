package codereview.simpleorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateOrderRequest {

    private List<OrderLineRequest> orderLineRequests;
}
