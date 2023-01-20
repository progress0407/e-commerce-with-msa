package codereview.simpleorder.application;

import codereview.simpleorder.domain.Item;
import codereview.simpleorder.dto.request.CreateItemRequest;
import codereview.simpleorder.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static Item createItem(CreateItemRequest request) {

        return new Item(request.getName(), request.getSize(), request.getPrice(), request.getAvailableQuantity());
    }
}
