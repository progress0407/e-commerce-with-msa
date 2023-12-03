package msa.with.ddd.item.presentation

import io.philo.dto.ResourceCreateResponse
import msa.with.ddd.item.domain.service.ItemService
import msa.with.ddd.item.presentation.dto.ItemCreateRequest
import msa.with.ddd.item.presentation.dto.ItemResponses
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*

@RequestMapping("/items")
@RestController
class ItemController(private val itemService: ItemService) {

    /**
     * 상품 등록
     */
    @PostMapping
    @ResponseStatus(CREATED)
    fun add(@RequestBody request: ItemCreateRequest): ResourceCreateResponse {

        val (name, size, price, stockQuantity) = request

        val itemId = itemService.registerItem(name, size, price, stockQuantity)

        return ResourceCreateResponse(itemId)
    }

    /**
     * @param itemIds null일 경우 모두 조회
     */
    @GetMapping
    fun list(@RequestParam(value = "ids", required = false) itemIds: List<Long>?): ItemResponses {

        val items = itemService.findItems(itemIds)

        return ItemResponses(items)
    }
}