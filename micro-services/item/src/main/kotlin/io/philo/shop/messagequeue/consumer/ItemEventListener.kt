package io.philo.shop.messagequeue.consumer

import io.philo.shop.domain.outbox.ItemOutBox
import io.philo.shop.domain.service.ItemService
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_QUEUE_NAME
import io.philo.shop.repository.ItemOutBoxRepository
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ItemEventListener(
    private val itemService: ItemService,
    private val itemOutBoxRepository: ItemOutBoxRepository,
) {

    private val log = KotlinLogging.logger { }

    /**
     * 주문 생성 이벤트 수신처
     */
    @RabbitListener(queues = [ORDER_CREATED_QUEUE_NAME])
    fun listenOrderCreatedEvent(event: OrderCreatedEvent) {

        log.info { "$event" }

        val orderLineEvents = event.orderLineCreatedEvents
        val itemVerification = itemService.checkItemBeforeOrder(orderLineEvents)
        if (itemVerification) {
            val itemMap = event.orderLineCreatedEvents.associateBy({ it.itemId }, { it.itemQuantity })
            itemService.decreaseItems(itemMap)
        }

        val outbox = ItemOutBox(event.orderId, itemVerification)
        itemOutBoxRepository.save(outbox)
    }
}
