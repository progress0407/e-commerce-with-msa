package io.philo.shop.restclient

import io.philo.shop.dto.web.ItemResponses
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "item-service", value = "item-service-call", url = "localhost:8000")
interface ItemFeignClient {

    @GetMapping("/items")
    fun requestItems(@RequestParam("ids") ids: List<Long>): ItemResponses
}