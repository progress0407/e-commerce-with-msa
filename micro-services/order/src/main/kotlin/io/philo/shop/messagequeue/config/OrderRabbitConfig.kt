package io.philo.shop.messagequeue.config

import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_QUEUE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_COUPON_ROUTING_KEY
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_QUEUE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_CREATED_TO_ITEM_ROUTING_KEY
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_FAILED_TO_COUPON_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_FAILED_TO_COUPON_QUEUE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_FAILED_TO_COUPON_ROUTING_KEY
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_FAILED_TO_ITEM_EXCHANGE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_FAILED_TO_ITEM_QUEUE_NAME
import io.philo.shop.order.OrderRabbitProperty.Companion.ORDER_FAILED_TO_ITEM_ROUTING_KEY
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

    /**
     * 주문 생성시 상품 서비스에 발행하는 이벤트
     */
    @Bean
    fun orderCreatedToItemQueue() = Queue(ORDER_CREATED_TO_ITEM_QUEUE_NAME)

    @Bean
    fun orderCreatedToItemExchange() = DirectExchange(ORDER_CREATED_TO_ITEM_EXCHANGE_NAME)

    @Bean
    fun orderCreatedToItemBinding(orderCreatedToItemQueue: Queue, orderCreatedToItemExchange: DirectExchange): Binding =
        BindingBuilder.bind(orderCreatedToItemQueue).to(orderCreatedToItemExchange).with(ORDER_CREATED_TO_ITEM_ROUTING_KEY)


    /**
     * 주문 생성시 쿠폰 서비스에 발행하는 이벤트
     */
    @Bean
    fun orderCreatedToCouponQueue() = Queue(ORDER_CREATED_TO_COUPON_QUEUE_NAME)

    @Bean
    fun orderCreatedToCouponExchange() = DirectExchange(ORDER_CREATED_TO_COUPON_EXCHANGE_NAME)

    @Bean
    fun orderCreatedToCouponBinding(orderCreatedToCouponQueue: Queue, orderCreatedToCouponExchange: DirectExchange): Binding =
        BindingBuilder.bind(orderCreatedToCouponQueue).to(orderCreatedToCouponExchange).with(ORDER_CREATED_TO_COUPON_ROUTING_KEY)

    /**
     * 주문 실패시 상품 서비스에 발행하는 보상 이벤트
     */
    @Bean
    fun orderFailedToItemQueue() = Queue(ORDER_FAILED_TO_ITEM_QUEUE_NAME)

    @Bean
    fun orderFailedToItemExchange() = DirectExchange(ORDER_FAILED_TO_ITEM_EXCHANGE_NAME)

    @Bean
    fun orderFailedToItemBinding(orderFailedToItemQueue: Queue, orderFailedToItemExchange: DirectExchange): Binding =
        BindingBuilder.bind(orderFailedToItemQueue).to(orderFailedToItemExchange).with(ORDER_FAILED_TO_ITEM_ROUTING_KEY)

    /**
     * 주문 실패시 쿠폰 서비스에 발행하는 보상 이벤트
     */
    @Bean
    fun orderFailedToCouponQueue() = Queue(ORDER_FAILED_TO_COUPON_QUEUE_NAME)

    @Bean
    fun orderFailedToCouponExchange() = DirectExchange(ORDER_FAILED_TO_COUPON_EXCHANGE_NAME)

    @Bean
    fun orderFailedToCouponBinding(orderFailedToCouponQueue: Queue, orderFailedToCouponExchange: DirectExchange): Binding =
        BindingBuilder.bind(orderFailedToCouponQueue).to(orderFailedToCouponExchange).with(ORDER_FAILED_TO_COUPON_ROUTING_KEY)


    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, messageConverter: Jackson2JsonMessageConverter): RabbitTemplate {

        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter

        return rabbitTemplate
    }
}