package io.philo.communication.item.httpclient

import io.philo.communication.item.httpclient.dto.ItemResponse
import org.springframework.stereotype.Component

@Component
class ItemHttpClient {

    fun getItemAmount(id: Long): ItemResponse {
        return ItemResponse(0, "", 0)
    }
}