package io.philo.shop.item

import io.philo.shop.item.dto.ItemInternalResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "item-service")
interface ItemFeignClient {

    @GetMapping("/items/internal")
    fun requestItems(@RequestParam("ids") ids: List<Long>): List<ItemInternalResponse>
}