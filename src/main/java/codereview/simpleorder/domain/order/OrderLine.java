package codereview.simpleorder.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "order")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_id")
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
    private int orderPrice;

    @Column(nullable = false)
    private int orderQuantity;

    public OrderLine(Long itemId, String itemName, String size, int orderPrice, int orderQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.size = size;
        this.orderPrice = orderPrice;
        this.orderQuantity = orderQuantity;
    }

    public void mapOrder(Order order) {
        this.order = order;
    }

    public int orderLineAmount() {
        return orderPrice * orderQuantity;
    }
}
