package io.philo.shop.item.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.shop.domain.entity.ItemEntity

class ItemTest : StringSpec({

    "구매한 수량만큼 재고가 감소한다" {

        // given
        val itemEntity = ItemEntity.fixture

        // when
        itemEntity.decreaseStockQuantity(2)

        // then
        itemEntity.stockQuantity shouldBe 2
    }

    "재고 수량보다 많은 수량을 구매할 수 없다" {

        // given
        val itemEntity = ItemEntity.fixture

        // when
        val exception = shouldThrow<IllegalStateException> {
            itemEntity.decreaseStockQuantity(5)
        }

        // then
        exception.message shouldBe "주문수량에 비해 상품의 재고수량이 충분하지 않습니다."
    }
})

val ItemEntity.Companion.fixture: ItemEntity
    get() = ItemEntity(name = "척 70 클래식 블랙 컨버스", size = "270", price = 86_000, stockQuantity = 4)
