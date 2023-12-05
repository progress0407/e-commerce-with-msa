package io.philo.shop.item

import io.philo.shop.item.dto.ItemResponse
import org.springframework.stereotype.Component

@Component
class ItemHttpClient {

    fun getItemAmount(id: Long): ItemResponse {
        return ItemResponse(0, "", 0)
    }
}