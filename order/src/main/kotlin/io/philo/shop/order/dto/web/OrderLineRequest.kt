package io.philo.shop.order.dto.web

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

@NoArgsConstructor
@AllArgsConstructor
@Getter
data class OrderLineRequest(
    val itemId: Long,
    val quantity: Int = 0
)