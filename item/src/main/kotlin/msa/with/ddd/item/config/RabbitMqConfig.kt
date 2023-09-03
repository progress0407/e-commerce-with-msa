package msa.with.ddd.item.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig {

    private val EXCAHGE_NAME = "item.decrease.stock.exchange"
    private val QUEUE_NAME = "item.decrease.stock.queue"
    private val ROUTING_KEY = "item.decrease.stock.routing.#"

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(EXCAHGE_NAME)
    }

    @Bean
    fun queue(): Queue {
        return Queue(QUEUE_NAME)
    }

    @Bean
    fun binding(queue: Queue?, exchange: TopicExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        return rabbitTemplate
    }
}