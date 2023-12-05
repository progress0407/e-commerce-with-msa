package io.philo.shop.communication.event

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
class RabbitMqConfig(
    @Value("\${spring.rabbitmq.host}")
    val host: String,

    @Value("\${spring.rabbitmq.port}")
    val port: Int,

    @Value("\${spring.rabbitmq.username}")
    val username: String,

    @Value("\${spring.rabbitmq.password}")
    val password: String,
) {


    private val EXCAHGE_NAME = "item.decrease.stock.exchange"
    private val QUEUE_NAME = "item.decrease.stock.queue"
    private val ROUTING_KEY = "item.decrease.stock.routing.#"

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun queue() = Queue(QUEUE_NAME)

    @Bean
    fun exchange() = DirectExchange(EXCAHGE_NAME)

    @Bean
    fun binding(
        queue: Queue,
        exchange: DirectExchange
    ): Binding =
        BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY)

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