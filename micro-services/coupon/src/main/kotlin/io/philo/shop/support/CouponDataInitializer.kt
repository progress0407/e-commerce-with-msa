package io.philo.shop.support

import io.philo.shop.domain.entity.core.FixedDiscountCouponEntity
import io.philo.shop.domain.entity.core.RatioDiscountCouponEntity
import io.philo.shop.domain.entity.core.UserCouponEntity
import io.philo.shop.domain.repository.CouponRepository
import io.philo.shop.domain.repository.UserCouponRepository
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class CouponDataInitializer(
    private val dataSource: DataSource,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
) {

    private val log = KotlinLogging.logger { }

    @EventListener
    fun onApplicationEvent(event: ApplicationStartedEvent) {
        initEntities()
    }

    /**
     * todo 추후 유저 서비스와 정합성을 고려하여 작성할 것
     */
    private fun initEntities() {

        val userId: Long = 1

        val firstRegisterCoupon = FixedDiscountCouponEntity("첫 가입 3,000원 할인 쿠폰", 3_000)
        val birthdayCoupon = RatioDiscountCouponEntity("초신사 생일 15% 할인 쿠폰", 15)
        couponRepository.saveAll(listOf(firstRegisterCoupon, birthdayCoupon))

        val sampleUserCouponEntity1 = UserCouponEntity(userId = userId, couponId = firstRegisterCoupon.id!!)
        val sampleUserCouponEntity2 = UserCouponEntity(userId = userId, couponId = birthdayCoupon.id!!)

        userCouponRepository.saveAll(listOf(sampleUserCouponEntity1, sampleUserCouponEntity2))
    }
}