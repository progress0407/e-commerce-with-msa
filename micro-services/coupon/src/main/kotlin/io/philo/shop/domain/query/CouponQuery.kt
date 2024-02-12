package io.philo.shop.domain.query

import io.philo.shop.domain.entity.core.CouponEntity
import io.philo.shop.domain.repository.CouponRepository
import io.philo.shop.domain.repository.ItemReplicaRepository
import io.philo.shop.domain.repository.UserCouponRepository
import io.philo.shop.domain.service.CouponDiscountCalculator
import io.philo.shop.error.EntityNotFoundException
import io.philo.shop.presentation.dto.CouponAppliedAmountResponseDto
import org.springframework.stereotype.Component

@Component
class CouponQuery(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val itemReplicaRepository: ItemReplicaRepository,
) {

    fun calculateAmount(userId: Long, itemId: Long, userCouponIds: List<Long>): CouponAppliedAmountResponseDto {

        val item = itemReplicaRepository.findById(itemId).orElseThrow { EntityNotFoundException(itemId) }
        val userCoupons = userCouponRepository.findAllByUserIdAndCouponIdIn(userId, userCouponIds)
        val couponIds = userCoupons.map { it.couponId }.toList()
        val coupons: List<CouponEntity> = couponRepository.findAllByIdIn(couponIds)

        val discountAmount = CouponDiscountCalculator.calculateDiscountAmount(item.itemAmount, coupons)

        return CouponAppliedAmountResponseDto(discountAmount)
    }
}