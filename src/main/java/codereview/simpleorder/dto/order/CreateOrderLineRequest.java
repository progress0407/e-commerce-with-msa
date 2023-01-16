package codereview.simpleorder.dto.order;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.domain.order.OrderLine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrderLineRequest {

    private Long itemId;
    private String size;
    private int quantity;

    public OrderLine toEntity(Item item) {
        return new OrderLine(itemId, item.getName(), size, item.getPrice(), quantity);
    }
}
