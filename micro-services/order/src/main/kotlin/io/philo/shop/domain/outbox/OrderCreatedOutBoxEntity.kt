package io.philo.shop.domain.outbox

import io.philo.shop.common.VerificationStatus
import io.philo.shop.common.VerificationStatus.PENDING
import io.philo.shop.common.VerificationStatus.SUCCESS
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING

/**
 * 주문 생성 후 이벤트 발행을 하기 위한 이벤트 저장 테이블
 */
@Entity
@Table(name = "order_created_out_box")
class OrderCreatedOutBoxEntity(

    @Column(nullable = false)
    val orderId: Long, // trace id 로서의 기능도 역할 가능하다 (혹은 transcation id)

    @Column(nullable = false)
    val requesterId: Long,

) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    private var loaded: Boolean = false // 발송 여부

    @Enumerated(STRING)
    @Column(nullable = false)
    var itemValidated: VerificationStatus = PENDING // 상품 서비스 유효성 체크

    @Enumerated(STRING)
    @Column(nullable = false)
    var couponValidated: VerificationStatus = PENDING // 쿠폰 서비스 유효성 체크

//    @Column(nullable = false)
//    var paymentValidated: Boolean = false // 결제 서비스 유효성 체크

    protected constructor () : this(0L, 0L)

    fun load() {
        this.loaded = true
    }

    fun changeItemValidated(verification: Boolean) {
        this.itemValidated = VerificationStatus.of(verification)
    }

    fun changeCouponValidated(verification: Boolean) {
        this.couponValidated = VerificationStatus.of(verification)
    }

    val isSuccess: Boolean
        get() = itemValidated == SUCCESS && couponValidated == SUCCESS
}