package io.philo.shop.messagequeue

import io.philo.shop.error.EntityNotFoundException
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_RES_QUEUE_NAME
import io.philo.shop.item.ItemVerificationEvent
import io.philo.shop.repository.OrderOutBoxRepository
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class OrderEventListener(private val orderOutBoxRepository: OrderOutBoxRepository) {

    private val log = KotlinLogging.logger { }

    @RabbitListener(queues = [ITEM_VERIFY_RES_QUEUE_NAME])
    fun listenItemVerification(event: ItemVerificationEvent) {

        val orderId = event.orderId
        val outBox = orderOutBoxRepository.findByOrderId(orderId) ?: throw EntityNotFoundException(orderId)
        outBox.changeItemValidated(event.verification)
        orderOutBoxRepository.save(outBox)
    }
}