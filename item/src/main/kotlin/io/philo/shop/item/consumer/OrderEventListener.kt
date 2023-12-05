package io.philo.shop.item.consumer

import io.philo.shop.item.consumer.dto.OrderCreatedEvent
import io.philo.shop.item.domain.service.ItemService
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
