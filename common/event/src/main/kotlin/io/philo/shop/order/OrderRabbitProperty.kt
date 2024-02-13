package io.philo.shop.order

class OrderRabbitProperty {


    companion object {
        private const val ORDER_CREATED_TOPIC_PREFIX = "order.created"

        const val ORDER_CREATED_QUEUE_NAME = "$ORDER_CREATED_TOPIC_PREFIX.queue"

        const val ORDER_CREATED_EXCHANGE_NAME = "$ORDER_CREATED_TOPIC_PREFIX.exchange"

        const val ORDER_CREATED_ROUTING_KEY = "$ORDER_CREATED_TOPIC_PREFIX.routing.#"
    }
}