package io.philo.shop

import io.philo.shop.item.ItemRabbitProperty

class CouponRabbitProperty {

    companion object {
        /**
         * 주문 생성 검증에 대한 응답
         */
        private const val COUPON_VERIFY_RES_TOPIC_PREFIX = "coupon.verification.for.created.order"
        const val COUPON_VERIFY_RES_QUEUE_NAME = "$COUPON_VERIFY_RES_TOPIC_PREFIX.queue"
        const val COUPON_VERIFY_RES_EXCHANGE_NAME = "$COUPON_VERIFY_RES_TOPIC_PREFIX.exchange"
        const val COUPON_VERIFY_RES_ROUTING_KEY = "$COUPON_VERIFY_RES_TOPIC_PREFIX.routing.#"

        /**
         * 주문 실패 검증에 대한 응답
         */
        private const val COUPON_VERIFY_FAIL_RES_TOPIC_PREFIX = "item.verification.for.failed.order"
        const val COUPON_VERIFY_FAIL_RES_QUEUE_NAME = "$COUPON_VERIFY_FAIL_RES_TOPIC_PREFIX.queue"
        const val COUPON_VERIFY_FAIL_RES_EXCHANGE_NAME = "$COUPON_VERIFY_FAIL_RES_TOPIC_PREFIX.exchange"
        const val COUPON_VERIFY_FAIL_RES_ROUTING_KEY = "$COUPON_VERIFY_FAIL_RES_TOPIC_PREFIX.routing.#"
    }
}