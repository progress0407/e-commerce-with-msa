package io.philo.shop.order

class OrderRabbitProperty {


    companion object {
        private const val TOPIC_PREFIX = "order.create"

        const val QUEUE_NAME = "$TOPIC_PREFIX.queue"

        const val EXCHANGE_NAME = "$TOPIC_PREFIX.exchange"

        const val ROUTING_KEY = "$TOPIC_PREFIX.routing.#"
    }
}