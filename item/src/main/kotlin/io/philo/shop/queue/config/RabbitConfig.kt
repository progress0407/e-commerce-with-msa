package io.philo.shop.queue.config

import io.philo.shop.item.ItemRabbitProperty.Companion.EXCHANGE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.QUEUE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ROUTING_KEY
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun queue() = Queue(QUEUE_NAME)

    @Bean
    fun exchange() = DirectExchange(EXCHANGE_NAME)

    @Bean
    fun binding(
        queue: Queue,
        exchange: DirectExchange
    ): Binding =
        BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY)
}