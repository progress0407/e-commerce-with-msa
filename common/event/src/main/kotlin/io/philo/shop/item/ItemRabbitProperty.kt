package io.philo.shop.item

class ItemRabbitProperty {

    companion object {

        /**
         * 주문 생성 검증에 대한 응답
         */
        private const val ITEM_VERIFY_RES_TOPIC_PREFIX = "item.verification.for.created.order"
        const val ITEM_VERIFY_RES_QUEUE_NAME = "$ITEM_VERIFY_RES_TOPIC_PREFIX.queue"
        const val ITEM_VERIFY_RES_EXCHANGE_NAME = "$ITEM_VERIFY_RES_TOPIC_PREFIX.exchange"
        const val ITEM_VERIFY_RES_ROUTING_KEY = "$ITEM_VERIFY_RES_TOPIC_PREFIX.routing.#"

        /**
         * 주문 실패 검증에 대한 응답
         */
        private const val ITEM_VERIFY_FAIL_RES_TOPIC_PREFIX = "item.verification.for.failed.order"
        const val ITEM_VERIFY_FAIL_RES_QUEUE_NAME = "$ITEM_VERIFY_FAIL_RES_TOPIC_PREFIX.queue"
        const val ITEM_VERIFY_FAIL_RES_EXCHANGE_NAME = "$ITEM_VERIFY_FAIL_RES_TOPIC_PREFIX.exchange"
        const val ITEM_VERIFY_FAIL_RES_ROUTING_KEY = "$ITEM_VERIFY_FAIL_RES_TOPIC_PREFIX.routing.#"

        /**
         * 쿠폰과 데이터 동기화를 하기 위한 상품 이벤트
         */
        private const val ITEM_REPLICA_FOR_COUPON_TOPIC_PREFIX = "item.replica.for.coupon"
        const val ITEM_REPLICA_FOR_COUPON_QUEUE_NAME = "$ITEM_REPLICA_FOR_COUPON_TOPIC_PREFIX.queue"
        const val ITEM_REPLICA_FOR_COUPON_RES_EXCHANGE_NAME = "$ITEM_REPLICA_FOR_COUPON_TOPIC_PREFIX.exchange"
        const val ITEM_REPLICA_FOR_COUPON_RES_ROUTING_KEY = "$ITEM_REPLICA_FOR_COUPON_TOPIC_PREFIX.routing.#"
    }
}