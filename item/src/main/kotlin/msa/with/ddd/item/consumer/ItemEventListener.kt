package msa.with.ddd.item.consumer

import msa.with.ddd.item.application.ItemService
import msa.with.ddd.item.dto.order.OrderCreatedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ItemEventListener(private val itemService: ItemService) {

    @EventListener
    fun captureEvent(event: OrderCreatedEvent) {
        val itemIdToDecreaseQuantity = event.values()
        itemService.decreaseItems(itemIdToDecreaseQuantity)
    }
}