package io.philo.shop.messagequeue.config

import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_QUEUE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_ROUTING_KEY
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig(
    @Value("\${spring.rabbitmq.host}")
    val host: String,

    @Value("\${spring.rabbitmq.port}")
    val port: Int,

    @Value("\${spring.rabbitmq.username}")
    val username: String,

    @Value("\${spring.rabbitmq.password}")
    val password: String,
) {

    @Bean
    fun queue() = Queue(ORDER_CREATED_QUEUE_NAME)

    @Bean
    fun exchange() = DirectExchange(ORDER_CREATED_EXCHANGE_NAME)

    @Bean
    fun binding(
        queue: Queue,
        exchange: DirectExchange
    ): Binding =
        BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ORDER_CREATED_ROUTING_KEY)

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory, // 위에서 다 주입된 Caching 구현체로, port, username등 다 포함됨
        messageConverter: Jackson2JsonMessageConverter
    ): RabbitTemplate {

//        var connectionFactory = CachingConnectionFactory()
//        connectionFactory.setHost(host)
//        connectionFactory.port=port
//        connectionFactory.username=username
//        connectionFactory.setPassword(password)

        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter

        return rabbitTemplate
    }
}