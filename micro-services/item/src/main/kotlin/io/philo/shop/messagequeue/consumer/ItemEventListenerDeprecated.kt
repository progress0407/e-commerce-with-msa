package io.philo.shop.messagequeue.consumer

import io.philo.shop.domain.service.ItemService
import io.philo.shop.order.OrderCreatedEventDeprecated
import mu.KotlinLogging

//@Component
@Deprecated("out box 패턴을 사용하면서 불필요하게 된 이벤트 리스너")
class ItemEventListenerDeprecated(private val itemService: ItemService) {

    private val log = KotlinLogging.logger { }

//    @RabbitListener(queues = [RabbitConfig.QUEUE_NAME])
    fun listenEvent(event: OrderCreatedEventDeprecated) {

        log.info { "$event" }

        val itemIdToDecreaseQuantity = event.values()

        itemService.decreaseItemsDeprecated(itemIdToDecreaseQuantity)
    }
}
