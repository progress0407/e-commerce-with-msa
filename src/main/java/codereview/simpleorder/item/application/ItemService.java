package codereview.simpleorder.item.application;

import codereview.simpleorder.item.domain.Item;
import codereview.simpleorder.item.dto.web.CreateItemRequest;
import codereview.simpleorder.item.dto.web.ItemResponse;
import codereview.simpleorder.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long registerItem(CreateItemRequest request) {

        Item item = createItem(request);
        Item savedItem = itemRepository.save(item);

        return savedItem.getId();
    }

    public List<ItemResponse> findItems(List<Long> itemIds) {

        if (itemIds == null || itemIds.isEmpty()) {

            return itemRepository.findAll().stream()
                    .map(ItemResponse::new)
                    .toList();
        }

        return itemRepository.findByIdIn(itemIds).stream()
                .map(ItemResponse::new)
                .toList();
    }

    @Transactional
    public void decreaseItems(Map<Long, Integer> itemIdToDecreaseQuantity) {

        Set<Long> itemIds = itemIdToDecreaseQuantity.keySet();
        List<Item> findItems = itemRepository.findByIdIn(itemIds); // problem !
        validateAndDecreaseItemQuantity(itemIdToDecreaseQuantity, findItems);
    }

    private static Item createItem(CreateItemRequest request) {

        return new Item(request.getName(), request.getSize(), request.getPrice(), request.getAvailableQuantity());
    }

    private static void validateAndDecreaseItemQuantity(Map<Long, Integer> itemIdToDecreaseQuantity, List<Item> findItems) {

        for (Item item : findItems) {
            int decreaseQuantity = itemIdToDecreaseQuantity.get(item.getId());
            item.decreaseQuantity(decreaseQuantity);
        }
    }
}
