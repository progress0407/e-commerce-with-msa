package io.philo.shop.presentation

import io.philo.shop.domain.repository.CouponRepository
import io.philo.shop.domain.repository.UserCouponRepository
import io.philo.shop.domain.service.CouponService
import io.philo.shop.presentation.dto.CouponListDto
import io.philo.shop.presentation.dto.UserCouponListDto
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
     * 고민:
     *
     * 시스템상 생성할 것인지 아니면,
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
    fun listOfUser(@PathVariable userId: Long): List<UserCouponListDto> {

        return userCouponRepository
            .findAll()
            .map { UserCouponListDto(it) }
    }

    @GetMapping("/coupon-applied-amount")
    fun calculateAmount(): Int {

//        todo return couponService.calculateAmount()
        return -1
    }
}