package io.philo.shop.presentation

import io.philo.shop.domain.repository.CouponRepository
import io.philo.shop.domain.repository.UserCouponRepository
import io.philo.shop.domain.service.CouponService
import io.philo.shop.presentation.dto.CouponListDto
import org.springframework.web.bind.annotation.*

@RequestMapping("/coupons")
@RestController
class CouponController(
    private val couponService: CouponService,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {

    /**
     * Coupon 생성
     *
     * 시스템적으로 생성할 것인지 아니면,
     *
     * 사람이 생성하게 둘 것인지
     */
    @PostMapping
    fun createCoupon(): Unit {

        return couponService.createCoupon()
    }

    @GetMapping
    fun list(): List<CouponListDto> {

        return couponRepository
            .findAll()
            .map { CouponListDto(it) }
    }

    @GetMapping("/users/{userId}")
    fun listOfUser(@PathVariable userId: Long): Unit {

//        return couponService.listOfUser(userId)
    }



    @GetMapping("/coupon-applied-amount")
    fun calculateAmount(): Int {

//        todo return couponService.calculateAmount()
        return -1
    }
}