package codereview.simpleorder.presentation.item;

import codereview.simpleorder.application.item.ItemService;
import codereview.simpleorder.dto.item.CreateItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerItem(@RequestBody CreateItemRequest request) {

        Long savedItem = itemService.registerItem(request);

        return savedItem;
    }
}
