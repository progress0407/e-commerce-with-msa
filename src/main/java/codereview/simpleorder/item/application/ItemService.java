package codereview.simpleorder.item.application;

import codereview.simpleorder.etc.OrderCreatedEvent;
import codereview.simpleorder.item.domain.Item;
import codereview.simpleorder.item.dto.CreateItemRequest;
import codereview.simpleorder.item.dto.ItemResponse;
import codereview.simpleorder.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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

    @EventListener
    public void decreaseItems(OrderCreatedEvent event) {

        Map<Long, Integer> itemIdToDecreaseQuantity = event.values();
        Set<Long> itemIds = itemIdToDecreaseQuantity.keySet();
        List<Item> findItems = itemRepository.findByIdIn(itemIds);
        decreaseItemQuantity(itemIdToDecreaseQuantity, findItems);
    }

    private static void decreaseItemQuantity(Map<Long, Integer> itemIdToDecreaseQuantity, List<Item> findItems) {

        for (Item item : findItems) {
            int decreaseQuantity = itemIdToDecreaseQuantity.get(item.getId());
            item.decreaseQuantity(decreaseQuantity);
        }
    }

    private static Item createItem(CreateItemRequest request) {

        return new Item(request.getName(), request.getSize(), request.getPrice(), request.getAvailableQuantity());
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
}
