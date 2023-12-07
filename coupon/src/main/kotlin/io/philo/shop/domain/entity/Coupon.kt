package io.philo.shop.domain.entity

import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Coupon(

    @field:Column(nullable = false)
    protected val expirationStartAt: LocalDate = LocalDate.now(),

    @field:Column(nullable = false)
    protected val expirationEndAt: LocalDate = LocalDate.now().plusDays(30)
): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null

    val order : Int
        get() = order()

    abstract fun order(): Int

    abstract fun discount(itemAmount: Int): Int
}