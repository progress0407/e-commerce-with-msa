package msa.with.ddd.item.web

import msa.with.ddd.item.domain.Item

class ItemResponse(item: Item) {

    val id: Long
    val name: String
    val size: String
    val price: Int
    val availableQuantity: Int

    init {
        id = item.id!!
        name = item.name
        size = item.size
        price = item.price
        availableQuantity = item.availableQuantity
    }
}