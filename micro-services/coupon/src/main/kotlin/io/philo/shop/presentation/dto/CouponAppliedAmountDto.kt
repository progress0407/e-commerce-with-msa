package io.philo.shop.presentation.dto

class CouponAppliedAmountDto

data class CouponAppliedAmountRequestDto(val itemId: Long, val userCouponIds: List<Long>)

data class CouponAppliedAmountResponseDto(val itemDiscountedAmount:Int)