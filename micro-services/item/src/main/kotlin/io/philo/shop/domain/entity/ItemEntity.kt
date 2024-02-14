package io.philo.shop.domain.entity

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "item",
    uniqueConstraints = [
        UniqueConstraint(name = "unique__item__name_size", columnNames = ["name", "size"])
    ]
)
class ItemEntity protected constructor(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    var name: String = "",

    @Column(nullable = false)
    var size: String = "-",

    @Column(nullable = false)
    var price: Int = 0,

    @Column(nullable = false)
    var stockQuantity: Int,
) {


    // default constructor for using JPA
    protected constructor() : this(id = null, name = "", size = "-", price = -1, stockQuantity = -1)

    // Size가 존재하지 않는 경우
    constructor(
        name: String,
        price: Int,
        stockQuantity: Int,
    ) : this(id = null, name, size = "-", price, stockQuantity)

    constructor(
        name: String,
        size: String,
        price: Int,
        stockQuantity: Int,
    ) : this(id = null, name, size, price, stockQuantity)

    companion object {}

    fun decreaseStockQuantity(orderQuantity: Int) {
        validateCanDecrease(orderQuantity)
        stockQuantity -= orderQuantity
    }

    fun increaseStockQuantity(orderQuantity: Int) {
        stockQuantity += orderQuantity
    }

    private fun validateCanDecrease(orderQuantity: Int) {
        check(stockQuantity - orderQuantity >= 0) {
            "주문수량에 비해 상품의 재고수량이 충분하지 않습니다."
        }
    }

    override fun toString(): String {
        return "Item(id=$id, name='$name', size='$size', price=$price, stockQuantity=$stockQuantity)"
    }
}
