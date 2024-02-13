package io.philo.shop.messagequeue.config

import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_QUEUE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_ROUTING_KEY
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_QUEUE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_ROUTING_KEY
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
class OrderRabbitConfig {

    @Bean
    fun orderCreatedToItemQueue() = Queue(ORDER_CREATED_TO_ITEM_QUEUE_NAME)

    @Bean
    fun orderCreatedToItemExchange() = DirectExchange(ORDER_CREATED_TO_ITEM_EXCHANGE_NAME)

    @Bean
    fun orderCreatedToItemBinding(orderCreatedToItemQueue: Queue, orderCreatedToItemExchange: DirectExchange): Binding =
        BindingBuilder.bind(orderCreatedToItemQueue).to(orderCreatedToItemExchange).with(ORDER_CREATED_TO_ITEM_ROUTING_KEY)


    @Bean
    fun orderCreatedToCouponQueue() = Queue(ORDER_CREATED_TO_COUPON_QUEUE_NAME)

    @Bean
    fun orderCreatedToCouponExchange() = DirectExchange(ORDER_CREATED_TO_COUPON_EXCHANGE_NAME)

    @Bean
    fun orderCreatedToCouponBinding(orderCreatedToCouponQueue: Queue, orderCreatedToCouponExchange: DirectExchange): Binding =
        BindingBuilder.bind(orderCreatedToCouponQueue).to(orderCreatedToCouponExchange).with(ORDER_CREATED_TO_COUPON_ROUTING_KEY)


    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, messageConverter: Jackson2JsonMessageConverter): RabbitTemplate {

        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter

        return rabbitTemplate
    }
}