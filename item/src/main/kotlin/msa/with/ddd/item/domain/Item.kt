package msa.with.ddd.item.domain

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.ToString

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
class Item(
    @field:Column(
        nullable = false,
        length = 100,
        unique = true
    ) val name: String,

    @field:Column(nullable = false) val size: String,

    @field:Column(nullable = false) val price: Int,

    @field:Column(nullable = false) var availableQuantity: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    val id: Long? = null

    @Version
    val version: Long? = null

    // default constructor for using JPA
    constructor() : this("", "", 0, 0)

    // Size가 존재하지 않는 경우
    constructor(name: String, price: Int, availableQuantity: Int) : this(name, "-", price, availableQuantity)

    fun decreaseQuantity(orderQuantity: Int) {
        validateCanDecrease(orderQuantity)
        availableQuantity -= orderQuantity
    }

    private fun validateCanDecrease(orderQuantity: Int) {
        check(availableQuantity - orderQuantity >= 0) { "주문수량에 비해 상품의 재고수량이 충분하지 않습니다." }
    }
}