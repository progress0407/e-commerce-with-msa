package io.philo.shop.order

/**
 * 주문 생성 | 실패에 대한 이벤트
 *
 * 현재 상품과 쿠폰에서 사용된다
 */
data class OrderChangedEvent(
    val orderId : Long,
    val requesterId: Long,
    val orderLineCreatedEvents: List<OrderLineCreatedEvent>
) {
    companion object
}
