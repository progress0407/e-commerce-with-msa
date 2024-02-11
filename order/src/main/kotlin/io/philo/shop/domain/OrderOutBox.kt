package io.philo.shop.domain

import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*

/**
 * 주문 생성 후 이벤트 발행을 하기 위한 이벤트 저장 테이블
 */
@Entity
class OrderOutBox(

    @Column(nullable = false)
    val orderId: Long,

    ) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null

    @Column(nullable = false)
    private var loaded: Boolean = false // 발송 여부

    @Column(nullable = false)
    var itemValidated: Boolean = false // 상품 서비스 유효성 체크

    @Column(nullable = false)
    var couponValidated: Boolean = false // 쿠폰 서비스 유효성 체크

    @Column(nullable = false)
    var paymentValidated: Boolean = false // 결제 서비스 유효성 체크

    protected constructor () : this(0L)

    fun load() {
        this.loaded = true
    }
}