package io.philo.shop.item

class ItemRabbitProperty {

    companion object {
        private const val ITEM_VERIFY_REQ_TOPIC_PREFIX = "item.verification.for.created.order"
        const val ITEM_VERIFY_REQ_QUEUE_NAME = "$ITEM_VERIFY_REQ_TOPIC_PREFIX.queue"
        const val ITEM_VERIFY_REQ_EXCHANGE_NAME = "$ITEM_VERIFY_REQ_TOPIC_PREFIX.exchange"
        const val ITEM_VERIFY_REQ_ROUTING_KEY = "$ITEM_VERIFY_REQ_TOPIC_PREFIX.routing.#"

        private const val ITEM_VERIFY_RES_TOPIC_PREFIX = "item.verification.for.created.order"
        const val ITEM_VERIFY_RES_QUEUE_NAME = "$ITEM_VERIFY_RES_TOPIC_PREFIX.queue"
        const val ITEM_VERIFY_RES_EXCHANGE_NAME = "$ITEM_VERIFY_RES_TOPIC_PREFIX.exchange"
        const val ITEM_VERIFY_RES_ROUTING_KEY = "$ITEM_VERIFY_RES_TOPIC_PREFIX.routing.#"

        private const val ITEM_REPLICA_FOR_COUPON_TOPIC_PREFIX = "item.verification.for.created.order"
        const val ITEM_REPLICA_FOR_COUPON_QUEUE_NAME = "$ITEM_VERIFY_REQ_TOPIC_PREFIX.queue"
        const val ITEM_REPLICA_FOR_COUPON_RES_EXCHANGE_NAME = "$ITEM_VERIFY_REQ_TOPIC_PREFIX.exchange"
        const val ITEM_REPLICA_FOR_COUPON_RES_ROUTING_KEY = "$ITEM_VERIFY_REQ_TOPIC_PREFIX.routing.#"
    }
}