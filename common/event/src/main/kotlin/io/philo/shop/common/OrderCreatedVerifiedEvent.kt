package io.philo.shop.common

/**
 * 주문 생성 후 협력 컴포넌트의 검증 결과값을 반환
 *
 * @param orderId 주문 번호 (trace id의 의미로 사용)
 * @param verification 검증 결과
 */
data class OrderCreatedVerifiedEvent(val orderId: Long, val verification: Boolean)
