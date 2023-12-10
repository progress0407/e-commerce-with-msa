package io.philo.shop.support

import io.philo.shop.domain.entity.FixedDiscountCoupon
import io.philo.shop.domain.entity.RatioDiscountCoupon
import io.philo.shop.domain.repository.CouponRepository
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class CouponDataInitializer(
    private val dataSource: DataSource,
    private val couponRepository: CouponRepository
){

    private val log = KotlinLogging.logger { }

    @EventListener
    fun onApplicationEvent(event: ApplicationStartedEvent) {
        initEntities()
    }

    private fun initEntities() {
        couponRepository.save(FixedDiscountCoupon(1_000))
        couponRepository.save(RatioDiscountCoupon(5))
        val findAll = couponRepository.findAll()
        println("findAll = ${findAll}")
    }
}