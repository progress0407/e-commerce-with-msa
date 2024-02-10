package io.philo.shop.integration

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.philo.shop.AcceptanceTest
import io.philo.shop.dto.ResourceCreateResponse
import io.philo.shop.dto.web.OrderCreateRequest
import io.philo.shop.dto.web.OrderLineRequestDto
import io.philo.shop.item.ItemRestClientFacade
import io.philo.shop.item.dto.ItemInternalResponseDto
import io.philo.shop.message.OrderEventPublisher
import io.philo.shop.repository.OrderRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

class OrderIntegrationTest : AcceptanceTest() {

    @Autowired
    lateinit var orderRepository: OrderRepository

    @MockkBean
    lateinit var itemClient: ItemRestClientFacade

    @MockkBean
    lateinit var orderEventPublisher: OrderEventPublisher

    /**
     * todo! 리펙터링하기
     *
     * - @mockkBean
     * - do nothing 모킹법 알기
     * - 테스트 픽스쳐 생각하기
     */
    @Test
    @Transactional(readOnly = true)
    fun `주문을 한다`() {

        // given
//        val itemClient = mockk<ItemRestClientFacade>()
        every { itemClient.requestItems(any()) } returns
                listOf(ItemInternalResponseDto(1L, "드로우핏 캐시미어 발마칸 코트 D.NABY", "M", 190_000))
        every { orderEventPublisher.publishEvent(any()) } just runs

        val requestBody = OrderCreateRequest(orderLineRequestDtos = listOf(OrderLineRequestDto(itemId = 1L, quantity = 5)))

        // when
        val createdEntityId = postAndGetBody<ResourceCreateResponse>("/orders", requestBody).id

        // then
        val foundEntity = orderRepository.findById(createdEntityId).get()
        val orderItems = foundEntity.orderLineItemEntities

        (createdEntityId > 0L) shouldBe true
        orderItems.size shouldBe 1
        val orderItem = orderItems[0]
        orderItem.itemId shouldBe 1L
        orderItem.orderedQuantity shouldBe 5
    }
}
