package io.philo.shop.message

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class RabbitMqEventListener(private val rabbitTemplate: RabbitTemplate){

    @RabbitListener(queues = ["item.decrease.stock.queue"])
    fun receiveMessage() {
        rabbitTemplate.receive()
    }
}