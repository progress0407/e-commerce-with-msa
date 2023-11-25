package io.philo.domain.service

import io.philo.domain.repository.CouponRepository
import io.philo.domain.repository.UserCouponRepository
import io.philo.communication.item.httpclient.ItemHttpClient
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val itemClient: ItemHttpClient,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {

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

            coupons
                .sortedBy { it.order }
                .map { it -> it.discount(itemAmount) }

        val finalAmount = coupons.sumOf { it.discount(itemAmount) }

        if (finalAmount < 0) {
            throw IllegalArgumentException("상품 가격은 음수가 될 수 없습니다")
        }

        return finalAmount
    }
}