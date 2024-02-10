package io.philo.shop.dto.web

import com.fasterxml.jackson.annotation.JsonCreator


data class OrderCreateRequest @JsonCreator constructor (
    val userId:Long, // 이 부분은 Redis with Netflix Passport로 없어질 수 있다
    val userCouponIds: List<Long>,
    val orderLineRequestDtos: List<OrderLineRequestDto>
)