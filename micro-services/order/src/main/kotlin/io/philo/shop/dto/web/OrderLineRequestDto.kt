package io.philo.shop.dto.web

import io.philo.shop.constant.ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE
import jakarta.validation.constraints.Size

data class OrderLineRequestDto(
    val itemId: Long,
    val itemQuantity: Int = 0,
    val itemAmount: Int = 0,
    val itemDiscountedAmount: Int = 0,

    @Size(max = 3, message = ITEM_COUPON_SIZE_APPLY_VALIDATION_MESSAGE)
    val userCouponIds: List<Long>? = null, // Coupon Id가 존재할 경우, 상품은 Discount되었다고 가정한다
)