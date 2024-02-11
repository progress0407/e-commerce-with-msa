package io.philo.shop.queue.consumer

import io.philo.shop.domain.service.ItemService
import io.philo.shop.order.OrderLineCreatedEvent
import io.philo.shop.order.OrderRabbitProperty.Companion.QUEUE_NAME
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class OrderCreatedEventListener(private val itemService: ItemService) {

    private val log = KotlinLogging.logger { }

    @RabbitListener(queues = [QUEUE_NAME])
    fun listenEvent(event: OrderLineCreatedEvent) {

        log.info { "$event" }

        val itemVerification = itemService.checkItemBeforeOrder(
            event.itemId,
            event.itemAmount,
            event.itemQuantity
        )

        if(itemVerification)
            println("todo")
//            itemService.decreaseItems()

        println("orderable = ${itemVerification}")
    }
}
