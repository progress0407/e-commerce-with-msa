package io.philo.shop.messagequeue.config

import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_QUEUE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_RES_EXCHANGE_NAME
import io.philo.shop.item.ItemRabbitProperty.Companion.ITEM_REPLICA_FOR_COUPON_RES_ROUTING_KEY
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
    fun itemVerifyReqQueue() = Queue(ITEM_VERIFY_REQ_QUEUE_NAME)

    @Bean
    fun itemVerifyReqExchange() = DirectExchange(ITEM_VERIFY_REQ_EXCHANGE_NAME)

    @Bean
    fun itemVerifyReqBinding(itemVerifyReqQueue: Queue, itemVerifyReqExchange: DirectExchange): Binding =
        BindingBuilder
            .bind(itemVerifyReqQueue)
            .to(itemVerifyReqExchange)
            .with(ITEM_VERIFY_REQ_ROUTING_KEY)

    @Bean
    fun itemReplicaForCouponQueue() = Queue(ITEM_REPLICA_FOR_COUPON_QUEUE_NAME)

    @Bean
    fun itemReplicaForCouponExchange() = DirectExchange(ITEM_REPLICA_FOR_COUPON_RES_EXCHANGE_NAME)

    @Bean
    fun itemReplicaForCouponBinding(itemReplicaForCouponQueue: Queue, itemReplicaForCouponExchange: DirectExchange): Binding =
        BindingBuilder
            .bind(itemReplicaForCouponQueue)
            .to(itemReplicaForCouponExchange)
            .with(ITEM_REPLICA_FOR_COUPON_RES_ROUTING_KEY)
}