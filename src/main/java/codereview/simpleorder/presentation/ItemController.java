package codereview.simpleorder.presentation;

import codereview.simpleorder.dto.item.ClothesResponse;
import codereview.simpleorder.dto.item.ClothesResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @GetMapping
    public ClothesResponses findClothes() {
        List<ClothesResponse> clothesResponses = List.of(new ClothesResponse(1L, "스웨터", 10_000));
        return new ClothesResponses(clothesResponses);
    }
}
