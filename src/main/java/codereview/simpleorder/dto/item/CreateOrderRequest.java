package codereview.simpleorder.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateOrderRequest {

    private List<CreateOrderLineRequest> createOrderLineRequests;
}
