package io.philo.shop.item

class ItemRabbitProperty {

    companion object {
        private const val TOPIC_PREFIX = "item.verification.for.created.order"

        const val QUEUE_NAME = "$TOPIC_PREFIX.queue"

        const val EXCHANGE_NAME = "$TOPIC_PREFIX.exchange"

        const val ROUTING_KEY = "$TOPIC_PREFIX.routing.#"
    }
}