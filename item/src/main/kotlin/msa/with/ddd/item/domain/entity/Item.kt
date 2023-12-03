package msa.with.ddd.item.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id

@Entity
class Item protected constructor(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    val id: Long? = null,

    @Column(nullable = false, length = 100, unique = true)
    var name: String = "",

    @Column(nullable = false)
    var size: String = "-",

    @Column(nullable = false)
    var price: Int = 0,

    @Column(nullable = false)
    var stockQuantity: Int
) {


    // default constructor for using JPA
    protected constructor() : this(id = null, name = "", size = "-", price = -1, stockQuantity = -1)

    // Size가 존재하지 않는 경우
    constructor(
        name: String,
        price: Int,
        stockQuantity: Int
    ) : this(id = null, name, size = "-", price, stockQuantity)

    constructor(
        name: String,
        size: String,
        price: Int,
        stockQuantity: Int
    ) : this(id = null, name, size, price, stockQuantity)

    companion object {}

    fun decreaseStockQuantity(orderQuantity: Int) {
        validateCanDecrease(orderQuantity)
        stockQuantity -= orderQuantity
    }

    private fun validateCanDecrease(orderQuantity: Int) {
        check(stockQuantity - orderQuantity >= 0) {
            "주문수량에 비해 상품의 재고수량이 충분하지 않습니다."
        }
    }
}