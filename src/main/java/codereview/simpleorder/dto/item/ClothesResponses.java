package codereview.simpleorder.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesResponses {
    private List<ClothesResponse> clothesResponses;
}
