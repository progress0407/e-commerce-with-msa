package codereview.simpleorder.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private int orderItemAmount;

    @Column(nullable = false)
    private int orderItemQuantity;

    public OrderItem(Long itemId, String itemName, String size, int orderItemAmount, int orderItemQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.size = size;
        this.orderItemAmount = orderItemAmount;
        this.orderItemQuantity = orderItemQuantity;
    }

    public void mapOrder(Order order) {
        this.order = order;
    }

    public int orderLineAmount() {
        return orderItemAmount * orderItemQuantity;
    }
}
