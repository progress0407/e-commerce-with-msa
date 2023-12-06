package io.philo.shop.consumer

import io.philo.shop.consumer.dto.OrderCreatedEvent
import io.philo.shop.domain.service.ItemService
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
