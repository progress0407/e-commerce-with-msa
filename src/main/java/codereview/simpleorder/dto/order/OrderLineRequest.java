package codereview.simpleorder.dto.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.domain.order.OrderLine;
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
