package io.philo.shop.queue.consumer

import io.philo.shop.domain.service.ItemService
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderRabbitProperty.Companion.QUEUE_NAME
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class OrderCreatedEventListener(private val itemService: ItemService) {

    private val log = KotlinLogging.logger { }

    @RabbitListener(queues = [QUEUE_NAME])
    fun listenEvent(event: OrderCreatedEvent) {

        log.info { "$event" }

        val orderLineEvents = event.orderLineCreatedEvents
        val itemVerification = itemService.checkItemBeforeOrder(orderLineEvents)

        println("itemVerification = ${itemVerification}")
    }
}
