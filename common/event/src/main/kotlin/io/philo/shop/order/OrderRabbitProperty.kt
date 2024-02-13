package io.philo.shop.order

class OrderRabbitProperty {


    companion object {
        /**
         * 주문 생성시 상품 서비스에 발행하는 이벤트
         */
        private const val ORDER_CREATED_TO_ITEM_TOPIC_PREFIX = "order.created.to.item"
        const val ORDER_CREATED_TO_ITEM_QUEUE_NAME = "$ORDER_CREATED_TO_ITEM_TOPIC_PREFIX.queue"
        const val ORDER_CREATED_TO_ITEM_EXCHANGE_NAME = "$ORDER_CREATED_TO_ITEM_TOPIC_PREFIX.exchange"
        const val ORDER_CREATED_TO_ITEM_ROUTING_KEY = "$ORDER_CREATED_TO_ITEM_TOPIC_PREFIX.routing.#"

        /**
         * 주문 생성시 쿠폰 서비스에 발행하는 이벤트
         */
        private const val ORDER_CREATED_TO_COUPON_TOPIC_PREFIX = "order.created.to.coupon"
        const val ORDER_CREATED_TO_COUPON_QUEUE_NAME = "$ORDER_CREATED_TO_COUPON_TOPIC_PREFIX.queue"
        const val ORDER_CREATED_TO_COUPON_EXCHANGE_NAME = "$ORDER_CREATED_TO_COUPON_TOPIC_PREFIX.exchange"
        const val ORDER_CREATED_TO_COUPON_ROUTING_KEY = "$ORDER_CREATED_TO_COUPON_TOPIC_PREFIX.routing.#"
    }
}