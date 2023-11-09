package msa.with.ddd.item.domain

import msa.with.ddd.item.domain.entity.Item
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ItemTest {

    @Test
    fun decreaseQuantity() {

        // item decrease and available quantity
        val item = Item()
        item.decreaseQuantity(1)
        val availableQuantity = item.availableQuantity

        Assertions.assertThat(availableQuantity).isEqualTo(0)
    }
}