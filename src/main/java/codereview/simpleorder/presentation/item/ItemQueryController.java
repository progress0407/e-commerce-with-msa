package codereview.simpleorder.presentation.item;

import codereview.simpleorder.dto.item.ItemResponse;
import codereview.simpleorder.dto.item.ItemResponses;
import codereview.simpleorder.repository.query.ItemQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemQueryController {

    private final ItemQueryRepository itemQueryRepository;

    @GetMapping
    public ItemResponses findClothes() {
        List<ItemResponse> items = itemQueryRepository.findAll();
        return new ItemResponses(items);
    }
}
