package codereview.simpleorder.dto.item;

import codereview.simpleorder.domain.item.Clothes;
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

    public OrderLine toEntity(Clothes clothes) {
        return new OrderLine(itemId, clothes.getName(), size, clothes.getPrice(), quantity);
    }
}
