package io.philo.shop.item

import io.philo.shop.item.dto.ItemInternalResponse
import org.springframework.stereotype.Component

@Component
class ItemRestClientFacade(private val itemHttpClient: ItemFeignClient) {

    fun requestItems(ids: List<Long>): List<ItemInternalResponse> {
        return itemHttpClient.requestItems(ids)
    }
}