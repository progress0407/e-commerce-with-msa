package io.philo.shop.domain.service

import io.philo.shop.domain.repository.CouponRepository
import io.philo.shop.domain.repository.UserCouponRepository
import io.philo.shop.domain.service.CouponDiscountCalculator.Companion.calculateDiscountAmount
import io.philo.shop.error.BadRequestException
import io.philo.shop.item.ItemRestClientFacade
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class CouponService(
    private val itemClient: ItemRestClientFacade,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {

    @Transactional
    fun createCoupon(): Unit {

//        couponRepository.save()
    }

    fun calculateAmount(
        userId: Long,
        itemId: Long,
        couponIds: List<Long>
    ): Int {

        // 해당 유저가 정말로 그 쿠폰을 가지고 있는지 유효성 체크 : UserCoupon
        // 이 부분은 결제할 떄 !?
        val coupons = couponRepository.findAllByIdIn(couponIds)

        // item 서비스에서 item 요청 + Feign Client Or GRPC
        val itemResponse = itemClient.getItemAmount(itemId)
        val itemAmount: Int = itemResponse.amount

        val finalAmount = calculateDiscountAmount(itemAmount, coupons)

        return finalAmount
    }

    fun calculateAmountForInternal(userId: Long, reqUserCouponIds: List<Long>): Int {

        val itemDtos = itemClient.requestItems(reqUserCouponIds)

        val userCoupons = userCouponRepository.findByUserIdAndCouponIdInAndIsUseFalse(userId, reqUserCouponIds)
        if(reqUserCouponIds.size != userCoupons.size)
            throw BadRequestException("사용자의 쿠폰에 대해 검증이 실패하였습니다.")

        return -1
    }
}