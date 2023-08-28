package msa.with.ddd.item.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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