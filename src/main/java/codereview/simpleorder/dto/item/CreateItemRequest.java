package codereview.simpleorder.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateItemRequest {

    private Long id;
    private String name;
    private String size;
    private int price;
    private int availableQuantity;
}
