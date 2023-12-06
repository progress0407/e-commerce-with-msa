package io.philo.shop.presentation

import io.philo.shop.domain.service.ItemService
import io.philo.shop.item.dto.ItemInternalResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/internal")
@RestController
class ItemInternalController(private val itemService: ItemService) {

    /**
     * @param itemIds null일 경우 모두 조회
     */
    @GetMapping
    fun list(@RequestParam(value = "ids") itemIds: List<Long>): List<ItemInternalResponse> {

        val dtos: List<ItemInternalResponse> = itemService.findItemsForInternal(itemIds)

        return dtos
    }
}