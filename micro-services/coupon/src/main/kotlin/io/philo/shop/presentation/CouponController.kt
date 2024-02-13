package io.philo.shop.presentation

import io.philo.shop.constant.SecurityConstant.Companion.LOGIN_USER_ID
import io.philo.shop.domain.core.CouponRepository
import io.philo.shop.domain.core.UserCouponRepository
import io.philo.shop.presentation.dto.CouponAppliedAmountRequestDto
import io.philo.shop.presentation.dto.CouponAppliedAmountResponseDto
import io.philo.shop.presentation.dto.CouponListDto
import io.philo.shop.presentation.dto.UserCouponListDto
import io.philo.shop.query.CouponQuery
import io.philo.shop.service.CouponService
import org.springframework.web.bind.annotation.*

@RequestMapping("/coupons")
@RestController
class CouponController(
    private val couponService: CouponService,
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val couponQuery: CouponQuery,
) {

    /**
     * Coupon 생성
     *
     * 고민:
     *
     * 오직 시스템으로 생성할 것인지 아니면,
     *
     * 유저도 생성 가능하게 할 것인지
     */
    @PostMapping
    fun createCoupon() {

        return couponService.createCoupon()
    }

    /**
     * 쇼핑몰에 존재하는 쿠폰 목록 조회
     */
    @GetMapping
    fun list(): List<CouponListDto> {

        return couponRepository
            .findAll()
            .map { CouponListDto(it) }
    }

    /**
     * 유저가 가지고 있는 쿠폰 목록 조회
     */
    @GetMapping("/users/{userId}")
    fun listOfUser(@PathVariable userId: Long): List<UserCouponListDto> {

        return userCouponRepository
            .findAll()
            .map { UserCouponListDto(it) }
    }

    /**
     * 할인된 상품 가격 정보를 보여준다
     */
    @GetMapping("/coupon-applied-amount")
    fun calculateAmount(
        @RequestHeader(name = LOGIN_USER_ID) userId: String,
        @RequestBody requestDto: CouponAppliedAmountRequestDto
    ): CouponAppliedAmountResponseDto {

        return couponQuery.calculateAmount(2L, requestDto.itemId, requestDto.userCouponIds)
    }
}