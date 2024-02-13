package io.philo.shop.service

import io.philo.shop.domain.core.CouponRepository
import io.philo.shop.domain.core.UserCouponRepository
import io.philo.shop.error.BadRequestException
import io.philo.shop.item.ItemRestClientFacade
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class CouponService(
    private val itemClient: ItemRestClientFacade,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun createCoupon(): Unit {

//        couponRepository.save()
    }

    fun calculateAmountForInternal(userId: Long, reqUserCouponIds: List<Long>): Int {

        val itemDtos = itemClient.requestItems(reqUserCouponIds)

        val userCoupons = userCouponRepository.findAllUsable(userId, reqUserCouponIds)
        if(reqUserCouponIds.size != userCoupons.size)
            throw BadRequestException("사용자의 쿠폰에 대해 검증이 실패하였습니다.")

        return -1
    }
}