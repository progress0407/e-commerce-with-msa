package codereview.simpleorder.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @Column(nullable = false)
    private int totalOrderAmount;

    public static Order create(List<OrderLine> orderLines) {
        return new Order(orderLines);
    }

    protected Order(List<OrderLine> orderLines) {
        this.totalOrderAmount = orderLines.stream()
                .mapToInt(OrderLine::orderLineAmount)
                .sum();
    }
}
