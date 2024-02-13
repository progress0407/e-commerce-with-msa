package io.philo.shop.domain.core

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserCouponRepository : JpaRepository<UserCouponEntity, Long> {

    fun findByUserId(userId: Long)

    fun findAllByUserIdAndCouponIdIn(userId: Long, couponIds:List<Long>): List<UserCouponEntity>

    /**
     * 활성화된 모든 쿠폰 찾기
     *
     * todo 일자까지 고려하기
     */
    @EntityGraph(attributePaths = ["coupon"])
    @Query("""
        select uc 
        from UserCouponEntity as uc
        join fetch uc.coupon c
        where uc.userId = :userId
        and uc.id in :userCouponIds
    """)
    fun findAllUsable(@Param("userId") userId: Long, @Param("userCouponIds") userCouponIds:List<Long>): List<UserCouponEntity>
}

