package io.philo.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Coupon(

    @field:Column(nullable = false)
    val expirationStartAt: LocalDate = LocalDate.now(),

    @field:Column(nullable = false)
    val expirationEndAt: LocalDate = LocalDate.now().plusDays(30)
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    val id: Long? = null

    val order : Int
        get() = order()

    abstract fun order(): Int

    abstract fun discount(itemAmount: Int): Int

}