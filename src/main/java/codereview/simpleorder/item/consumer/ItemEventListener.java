package codereview.simpleorder.item.consumer;

import codereview.simpleorder.item.application.ItemService;
import codereview.simpleorder.item.dto.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemEventListener {

    private final ItemService itemService;

    @TransactionalEventListener
    public void captureEvent(OrderCreatedEvent event) {

        Map<Long, Integer> itemIdToDecreaseQuantity = event.values();
        itemService.decreaseItems(itemIdToDecreaseQuantity);
    }
}
