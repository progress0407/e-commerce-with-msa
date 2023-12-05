package io.philo.shop.item.integartion

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.philo.shop.AcceptanceTest
import io.philo.shop.dto.ResourceCreateResponse
import io.philo.shop.item.domain.entity.Item
import io.philo.shop.item.presentation.dto.ItemCreateRequest
import io.philo.shop.item.presentation.dto.ItemResponse
import io.philo.shop.item.presentation.dto.ItemResponses
import org.junit.jupiter.api.Test

class ItemAcceptanceTest : AcceptanceTest() {


    @Test
    fun `재고 등록 및 조회`() {

        // given
        val requestBody = ItemCreateRequest.fixture

        // when
        val itemId = postAndGetBody<ResourceCreateResponse>("/items", requestBody).id
        val responseBody: ItemResponses = getAndGetBody<ItemResponses>("/items?ids=$itemId")

        // then
        val foundItems: List<ItemResponse> = responseBody.items

        itemId shouldNotBe null
        foundItems shouldNotBe null

        val foundItem = foundItems[0]

        foundItem.size shouldBe ItemCreateRequest.fixture.size
        foundItem.name shouldBe ItemCreateRequest.fixture.name
    }

    val ItemCreateRequest.Companion.fixture: ItemCreateRequest
        get() {

            val entity = io.philo.shop.item.domain.entity.Item.fixture

            return ItemCreateRequest(
                name = entity.name,
                size = entity.size,
                price = entity.price,
                stockQuantity = entity.stockQuantity
            )
        }

    val Item.Companion.fixture
        get() = Item(name = "컨셉원 슬랙스 BLACK 30",
            size = "30",
            price = 70_000,
            stockQuantity = 500)
}