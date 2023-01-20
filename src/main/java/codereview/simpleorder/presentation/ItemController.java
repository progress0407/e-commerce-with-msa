package codereview.simpleorder.presentation;

import codereview.simpleorder.application.ItemService;
import codereview.simpleorder.dto.request.CreateItemRequest;
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

        Long savedItemId = itemService.registerItem(request);

        return savedItemId;
    }
}
