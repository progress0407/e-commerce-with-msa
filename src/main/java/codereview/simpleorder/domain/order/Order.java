package codereview.simpleorder.domain.order;

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
    private List<OrderLine> orderLines = new ArrayList<>();

    @Column(nullable = false)
    private int totalOrderAmount;

    public static Order createOrder(List<OrderLine> orderLines) {

        return new Order(orderLines);
    }

    protected Order(List<OrderLine> orderLines) {

        this.orderLines = orderLines;
        mapOrder(orderLines);
        this.totalOrderAmount = calculateTotalOrderAmount(orderLines);
    }

    private int calculateTotalOrderAmount(List<OrderLine> orderLines) {

        return orderLines.stream()
                .mapToInt(OrderLine::orderLineAmount)
                .sum();
    }

    private void mapOrder(List<OrderLine> orderLines) {

        for (OrderLine orderLine : orderLines) {
            orderLine.mapOrder(this);
        }
    }
}
