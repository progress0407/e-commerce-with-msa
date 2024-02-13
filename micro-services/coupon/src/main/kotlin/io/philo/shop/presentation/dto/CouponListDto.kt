package io.philo.shop.presentation.dto

import io.philo.shop.domain.core.CouponEntity
import java.time.LocalDate

data class CouponListDto(val id: Long, val name: String, val expirationStartAt: LocalDate, val expirationEndAt: LocalDate) {

    constructor(entity: CouponEntity) : this(entity.id!!, entity.name, entity.expirationStartAt, entity.expirationEndAt)
}