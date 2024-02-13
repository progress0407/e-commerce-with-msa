package io.philo.shop.domain.outbox

import io.philo.shop.common.VerificationStatus
import io.philo.shop.common.VerificationStatus.PENDING
import io.philo.shop.common.VerificationStatus.SUCCESS
import io.philo.shop.entity.OutboxBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

/**
 * 주문 생성 후 이벤트 발행을 하기 위한 이벤트 저장 테이블
 */
@Entity
@Table(name = "order_created_outbox")
class OrderCreatedOutboxEntity(traceId:Long, requesterId:Long) : OutboxBaseEntity(traceId, requesterId) {

    @Enumerated(STRING)
    @Column(nullable = false)
    var itemValidated: VerificationStatus = PENDING // 상품 서비스 유효성 체크

    @Enumerated(STRING)
    @Column(nullable = false)
    var couponValidated: VerificationStatus = PENDING // 쿠폰 서비스 유효성 체크

    protected constructor () : this(0L, 0L)

    fun changeItemValidated(verification: Boolean) {
        this.itemValidated = VerificationStatus.of(verification)
    }

    fun changeCouponValidated(verification: Boolean) {
        this.couponValidated = VerificationStatus.of(verification)
    }

    val isSuccess: Boolean
        get() = itemValidated == SUCCESS && couponValidated == SUCCESS
}