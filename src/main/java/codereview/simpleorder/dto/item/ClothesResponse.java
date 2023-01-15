package codereview.simpleorder.dto.item;

import codereview.simpleorder.domain.item.Clothes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesResponse {

    private Long id;
    private String name;
    private String size;
    private int price;
    private int availableQuantity;

    public ClothesResponse(Clothes clothes) {
        this.id = clothes.getId();
        this.name = clothes.getName();
        this.size = clothes.getSize();
        this.price = clothes.getPrice();
        this.availableQuantity = clothes.getAvailableQuantity();
    }
}
