package io.philo.shop.messagequeue.consumer

import io.philo.shop.common.InAppEventListener
import io.philo.shop.domain.service.ItemEventService
import io.philo.shop.order.OrderCreatedEvent
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_QUEUE_NAME
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener

@InAppEventListener
class ItemEventListener(private val itemEventService: ItemEventService) {

    private val log = KotlinLogging.logger { }

    /**
     * 주문 생성 이벤트 수신처
     */
    @RabbitListener(queues = [ORDER_CREATED_TO_ITEM_QUEUE_NAME])
    fun listenOrderCreatedEvent(event: OrderCreatedEvent) {

        log.info { "$event" }

        itemEventService.listenOrderCreatedEvent(event)
    }
}
