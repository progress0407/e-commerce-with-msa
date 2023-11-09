package msa.with.ddd.item.consumer

import msa.with.ddd.item.domain.service.ItemService
import msa.with.ddd.item.consumer.dto.OrderCreatedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OrderEventListener(private val itemService: ItemService) {

    @EventListener
    fun listenEvent(event: OrderCreatedEvent) {
        val itemIdToDecreaseQuantity = event.values()
        itemService.decreaseItems(itemIdToDecreaseQuantity)
    }
}