package io.philo.shop.messagequeue

import io.philo.shop.CouponRabbitProperty.Companion.COUPON_VERIFY_RES_EXCHANGE_NAME
import io.philo.shop.CouponRabbitProperty.Companion.COUPON_VERIFY_RES_QUEUE_NAME
import io.philo.shop.CouponRabbitProperty.Companion.COUPON_VERIFY_RES_ROUTING_KEY
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CouponRabbitConfig {

    @Bean
    fun couponVerifyResQueue() = Queue(COUPON_VERIFY_RES_QUEUE_NAME)

    @Bean
    fun couponVerifyResExchange() = DirectExchange(COUPON_VERIFY_RES_EXCHANGE_NAME)

    @Bean
    fun couponVerifyResBinding(
        queue: Queue,
        exchange: DirectExchange,
    ): Binding =
        BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(COUPON_VERIFY_RES_ROUTING_KEY)

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory, // 위에서 다 주입된 Caching 구현체로, port, username등 다 포함됨
        messageConverter: Jackson2JsonMessageConverter,
    ): RabbitTemplate {

        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter

        return rabbitTemplate
    }
}