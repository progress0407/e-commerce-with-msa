package codereview.simpleorder.item.ui;

import codereview.simpleorder.item.application.ItemService;
import codereview.simpleorder.item.dto.CreateItemRequest;
import codereview.simpleorder.item.dto.ItemResponse;
import codereview.simpleorder.item.dto.ItemResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerItem(@RequestBody CreateItemRequest request) {

        Long savedItemId = itemService.registerItem(request);

        return savedItemId;
    }

    @GetMapping
    public ItemResponses findItems(@RequestParam(value = "ids", required = false) List<Long> itemIds) {

        List<ItemResponse> items = itemService.findItems(itemIds);

        return new ItemResponses(items);
    }
}
