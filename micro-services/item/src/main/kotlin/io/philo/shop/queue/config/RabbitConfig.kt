package io.philo.shop.queue.config

import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_REQ_EXCHANGE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_REQ_QUEUE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_VERIFY_REQ_ROUTING_KEY
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun queue() = Queue(ITEM_VERIFY_REQ_QUEUE_NAME)

    @Bean
    fun exchange() = DirectExchange(ITEM_VERIFY_REQ_EXCHANGE_NAME)

    @Bean
    fun binding(
        queue: Queue,
        exchange: DirectExchange
    ): Binding =
        BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ITEM_VERIFY_REQ_ROUTING_KEY)
}