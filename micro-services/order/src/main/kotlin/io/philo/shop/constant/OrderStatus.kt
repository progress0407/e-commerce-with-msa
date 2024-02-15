package io.philo.shop.constant

enum class OrderStatus(val koreanName: String, val description: String) {

    PENDING("주문 대기", "검증 이벤트 대기 중"),
    SUCCESS("주문 성공", "모든 검증 완료"),
    FAIL("주문 실패", "하나 이상의 이벤트 실패"),
    CANCEL("주문 취소", "보상 트랜잭션 처리 이후 주문 취소") // todo 구현할 지 고민
}