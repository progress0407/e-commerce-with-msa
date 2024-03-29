package io.philo.shop.domain.core

import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "coupon")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class CouponEntity(

    @field:Column(nullable = false)
    val name: String = "",

    @field:Column(nullable = false)
    val expirationStartAt: LocalDate = LocalDate.now(),

    @field:Column(nullable = false)
    val expirationEndAt: LocalDate = LocalDate.now().plusDays(30)

): BaseEntity() {

    val order : Int
        get() = order()

    abstract fun order(): Int

    abstract fun discount(itemAmount: Int): Int
}