package msa.with.ddd.item.integartion

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.philo.AcceptanceTest
import io.philo.dto.ResourceCreateResponse
import msa.with.ddd.item.domain.entity.Item
import msa.with.ddd.item.presentation.dto.ItemCreateRequest
import msa.with.ddd.item.presentation.dto.ItemResponses
import org.junit.jupiter.api.Test

class ItemAcceptanceTest : AcceptanceTest() {


    @Test
    fun `재고 등록 및 조회`() {

        // given
        val requestBody = ItemCreateRequest.fixture

        // when
        val itemId = postAndGetBody<ResourceCreateResponse>("/items", requestBody).id
        val responseBody = getAndGetBody<ItemResponses>("/items?ids=$itemId")

        // then
        val foundItems = responseBody.items
        itemId shouldNotBe null
        foundItems shouldNotBe null
        foundItems!!.size shouldBe 1
        foundItems[0].name shouldBe ItemCreateRequest.fixture.name
    }

    val ItemCreateRequest.Companion.fixture: ItemCreateRequest
        get() {

            val entity = Item.fixture

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