package msa.with.ddd.item.ui

import msa.with.ddd.item.application.ItemService
import msa.with.ddd.item.dto.item.ItemCreateRequest
import msa.with.ddd.item.dto.item.ItemResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/items")
@RestController
class ItemController(private val itemService: ItemService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody request: ItemCreateRequest): Long {
        return itemService.registerItem(request)
    }

    @GetMapping
    fun list(@RequestParam(value = "ids", required = false) itemIds: List<Long>?): ItemResponses {
        val items = itemService.findItems(itemIds)
        return ItemResponses(items)
    }
}