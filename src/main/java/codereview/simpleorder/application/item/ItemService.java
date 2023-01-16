package codereview.simpleorder.application.item;

import codereview.simpleorder.domain.item.Item;
import codereview.simpleorder.dto.item.CreateItemRequest;
import codereview.simpleorder.repository.command.ItemRepository;
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

        Item item = createImte(request);
        Item savedItem = itemRepository.save(item);

        return savedItem.getId();
    }

    private static Item createImte(CreateItemRequest request) {
        return new Item(request.getName(), request.getSize(), request.getPrice(), request.getAvailableQuantity());
    }
}
