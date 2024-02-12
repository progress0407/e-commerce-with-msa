package io.philo.shop.domain.repository

import io.philo.shop.domain.entity.core.UserCouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponRepository : JpaRepository<UserCouponEntity, Long> {

    fun findByUserId(userId: Long)

    fun findAllByUserIdAndCouponIdIn(userId: Long, couponIds:List<Long>): List<UserCouponEntity>

    /**
     * select *
     *
     * from user_coupon
     *
     * where id = :userId
     *
     * and coupon_id in (:coupon_id_1, :coupon_id_2, ...)
     *
     * and is_use = false
     *
     */
    fun findByUserIdAndCouponIdInAndIsUseFalse(userId: Long, couponIds:List<Long>): List<UserCouponEntity>
}

