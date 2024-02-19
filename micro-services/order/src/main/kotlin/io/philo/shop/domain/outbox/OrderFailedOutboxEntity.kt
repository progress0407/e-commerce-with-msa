package io.philo.shop.domain.outbox

import io.philo.shop.common.VerificationStatus
import io.philo.shop.common.VerificationStatus.*
import io.philo.shop.entity.OutboxBaseEntity
import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING

/**
 * 주문 실패 후 이벤트 발행을 하기 위한 이벤트 저장 테이블
 */
@Entity
@Table(name = "order_failed_outbox")
class OrderFailedOutboxEntity(

    traceId: Long,

    requesterId: Long,

    @Column(nullable = false)
    val isCompensatingItem: Boolean = false,

    @Column(nullable = false)
    val isCompensatingCoupon: Boolean = false,

    ) : OutboxBaseEntity(traceId, requesterId) {

    @Enumerated(STRING)
    @Column(nullable = false)
    var itemValidated: VerificationStatus = PENDING // 상품 서비스 유효성 체크

    @Enumerated(STRING)
    @Column(nullable = false)
    var couponValidated: VerificationStatus = PENDING // 쿠폰 서비스 유효성 체크

    protected constructor () : this(traceId = 0L, requesterId = 0L, isCompensatingItem = false, isCompensatingCoupon = false)

    fun changeItemValidated(verification: Boolean) {
        this.itemValidated = VerificationStatus.of(verification)
    }

    fun changeCouponValidated(verification: Boolean) {
        this.couponValidated = VerificationStatus.of(verification)
    }

    val isSuccess: Boolean
        get() = itemValidated == SUCCESS && couponValidated == SUCCESS

    val isCanceled: Boolean
        get() {
            return if (isCompensatingItem && isCompensatingCoupon)
                itemValidated == FAIL && couponValidated == FAIL
            else if(isCompensatingItem.not() && isCompensatingCoupon)
                couponValidated == FAIL
            else if(isCompensatingItem && isCompensatingCoupon.not())
                itemValidated == FAIL
            else false
        }

    val isDone: Boolean
        get() = itemValidated != PENDING && couponValidated != PENDING

    companion object {}
}
