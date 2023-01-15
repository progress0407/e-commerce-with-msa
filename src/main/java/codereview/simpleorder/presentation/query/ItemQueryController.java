package codereview.simpleorder.presentation.query;

import codereview.simpleorder.dto.item.ClothesResponse;
import codereview.simpleorder.dto.item.ClothesResponses;
import codereview.simpleorder.repository.query.ClothesQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemQueryController {

    private final ClothesQueryRepository clothesQueryRepository;

    @GetMapping
    public ClothesResponses findClothes() {
        List<ClothesResponse> clothes = clothesQueryRepository.findAll();
        return new ClothesResponses(clothes);
    }
}
