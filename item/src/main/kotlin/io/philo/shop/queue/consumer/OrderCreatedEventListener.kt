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

        var finalVerify = true
        for (event in event.orderLineCreatedEvents) {
            val itemVerification = itemService.checkItemBeforeOrder(
                event.itemId,
                event.itemAmount,
                event.itemQuantity
            )
            if (itemVerification == false) {
                finalVerify=false
            }
        }

        println("finalVerify = ${finalVerify}")
//        if(finalVerify)
//            println("todo")
//            itemService.decreaseItems()

//        println("orderable = ${itemVerification}")
    }
}
