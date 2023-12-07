package io.philo.shop.consumer

import io.philo.shop.domain.service.ItemService
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.RabbitConfig
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class OrderEventListener(private val itemService: ItemService) {

    private val log = KotlinLogging.logger { }

    @RabbitListener(queues = [RabbitConfig.QUEUE_NAME])
    fun listenEvent(event: OrderCreatedEvent) {

        log.info { "$event" }

        val itemIdToDecreaseQuantity = event.values()

        itemService.decreaseItems(itemIdToDecreaseQuantity)
    }

/*
    @RabbitListener(queues = ["order.create.queue"])
    fun listenEvent(event: String) {

        log.info { "$event" }
    }
*/
}
