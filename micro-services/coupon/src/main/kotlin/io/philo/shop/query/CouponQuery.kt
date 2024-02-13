package io.philo.shop.query

import io.philo.shop.domain.core.CouponRepository
import io.philo.shop.domain.core.UserCouponRepository
import io.philo.shop.domain.replica.ItemReplicaRepository
import io.philo.shop.error.EntityNotFoundException
import io.philo.shop.presentation.dto.CouponAppliedAmountResponseDto
import io.philo.shop.service.CouponDiscountCalculator
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponQuery(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val itemReplicaRepository: ItemReplicaRepository,
) {

    @Transactional(readOnly = false)
    fun calculateAmount(
        userId: Long,
        itemId: Long,
        userCouponIds: List<Long>,
    ): CouponAppliedAmountResponseDto {

        val item = itemReplicaRepository.findById(itemId).orElseThrow { EntityNotFoundException(itemId) }
        val userCoupons = userCouponRepository.findAllByUserIdAndCouponIdIn(userId, userCouponIds)
        val couponIds = userCoupons.map { it.coupon.id!! }.toList()
        val coupons = couponRepository.findAllByIdIn(couponIds)

        val discountAmount = CouponDiscountCalculator.calculateDiscountAmount(item.itemAmount, coupons)

        return CouponAppliedAmountResponseDto(discountAmount)
    }
}