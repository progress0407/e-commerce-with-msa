package codereview.simpleorder.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderLineRequest {

    private Long itemId;
    private int quantity;
}
