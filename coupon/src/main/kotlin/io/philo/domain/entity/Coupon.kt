package io.philo.domain.entity

import java.time.LocalDate

open abstract class Coupon(
    var expirationStartAt: LocalDate = LocalDate.now(),
    var expirationEndAt: LocalDate = LocalDate.now().plusDays(30)
) {

    abstract fun discount(itemAmount: Int): Int
}