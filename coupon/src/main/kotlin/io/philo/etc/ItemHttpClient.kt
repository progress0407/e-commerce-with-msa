package io.philo.etc

import org.springframework.stereotype.Component

@Component
class ItemHttpClient {

    fun getItemAmount(id: Long): ItemResponse {
        return ItemResponse(0, "", 0)
    }
}