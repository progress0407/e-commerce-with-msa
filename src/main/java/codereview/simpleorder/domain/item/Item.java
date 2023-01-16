package codereview.simpleorder.domain.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int availableQuantity;

    @Version
    private Long version;

    public Item(String name, String size, int price, int availableQuantity) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public void decreaseQuantity(int orderQuantity) {
        validateCanDecrease(orderQuantity);
        this.availableQuantity -= orderQuantity;
    }

    private void validateCanDecrease(int orderQuantity) {
        if (availableQuantity - orderQuantity < 0) {
            throw new IllegalArgumentException("주문수량에 비해 상품의 재고수량이 충분하지 않습니다.");
        }
    }
}
