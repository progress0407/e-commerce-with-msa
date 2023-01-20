package codereview.simpleorder.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "orderLines")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private int totalOrderAmount;

    public static Order createOrder(List<OrderItem> orderItems) {

        return new Order(orderItems);
    }

    protected Order(List<OrderItem> orderItems) {

        this.orderItems = orderItems;
        mapOrder(orderItems);
        this.totalOrderAmount = calculateTotalOrderAmount(orderItems);
    }

    private int calculateTotalOrderAmount(List<OrderItem> orderItems) {

        return orderItems.stream()
                .mapToInt(OrderItem::orderLineAmount)
                .sum();
    }

    private void mapOrder(List<OrderItem> orderItems) {

        for (OrderItem orderItem : orderItems) {
            orderItem.mapOrder(this);
        }
    }
}
