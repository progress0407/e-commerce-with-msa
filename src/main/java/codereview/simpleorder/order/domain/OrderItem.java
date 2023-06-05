package codereview.simpleorder.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "order")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(nullable = false, length = 100)
    private Long itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int orderItemPrice;

    @Column(nullable = false)
    private int orderItemQuantity;

    public OrderItem(Long itemId, String itemName, String size, int orderItemPrice, int orderItemQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.size = size;
        this.orderItemPrice = orderItemPrice;
        this.orderItemQuantity = orderItemQuantity;
    }

    public void mapOrder(Order order) {
        this.order = order;
    }

    public int orderItemAmount() {
        return orderItemPrice * orderItemQuantity;
    }
}
