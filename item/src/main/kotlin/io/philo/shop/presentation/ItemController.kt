package io.philo.shop.presentation

import io.philo.shop.domain.service.ItemService
import io.philo.shop.dto.ResourceCreateResponse
import io.philo.shop.presentation.dto.ItemCreateRequest
import io.philo.shop.presentation.dto.ItemResponse
import io.philo.shop.presentation.dto.ItemResponses
import mu.KotlinLogging
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*

@RequestMapping("/items")
@RestController
class ItemController(private val itemService: ItemService) {

    private val log = KotlinLogging.logger {  }

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
    fun list(@RequestParam(name = "ids", required = false) itemIds: List<Long>?,
             @RequestHeader(name = "user-passport", required = false) userHeader: String?
    ): ItemResponses {

        val items: List<ItemResponse> = itemService.findItems(itemIds)

        log.info { "# user header: ${userHeader}" }

        return ItemResponses(items)
    }
}