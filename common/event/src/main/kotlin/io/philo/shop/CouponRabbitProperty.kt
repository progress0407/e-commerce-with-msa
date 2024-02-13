package io.philo.shop

class CouponRabbitProperty {

    companion object {
        private const val COUPON_VERIFY_RES_TOPIC_PREFIX = "coupon.verification.for.created.order"
        const val COUPON_VERIFY_RES_QUEUE_NAME = "$COUPON_VERIFY_RES_TOPIC_PREFIX.queue"
        const val COUPON_VERIFY_RES_EXCHANGE_NAME = "$COUPON_VERIFY_RES_TOPIC_PREFIX.exchange"
        const val COUPON_VERIFY_RES_ROUTING_KEY = "$COUPON_VERIFY_RES_TOPIC_PREFIX.routing.#"
    }
}