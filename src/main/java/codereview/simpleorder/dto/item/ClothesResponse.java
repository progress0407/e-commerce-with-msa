package codereview.simpleorder.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesResponse {

    private Long id;
    private String name;
    private int price;
}
