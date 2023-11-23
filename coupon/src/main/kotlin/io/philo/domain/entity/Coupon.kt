package io.philo.domain.entity

import java.time.LocalDate

abstract class Coupon(
    var expirationStartAt: LocalDate = LocalDate.now(),
    var expirationEndAt: LocalDate = LocalDate.now().plusDays(30)
) {

    abstract val order : Int
        get() = order()
    abstract fun order(): Int

    abstract fun discount(itemAmount: Int): Int

    val calculatedValue: Int
        get() =         // 여기에 계산 로직을 구현합니다.
            42 // 예시 값

}