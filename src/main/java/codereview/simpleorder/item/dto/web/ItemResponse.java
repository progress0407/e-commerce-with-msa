package codereview.simpleorder.item.dto.web;

import codereview.simpleorder.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private String size;
    private int price;
    private int availableQuantity;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.size = item.getSize();
        this.price = item.getPrice();
        this.availableQuantity = item.getAvailableQuantity();
    }
}
