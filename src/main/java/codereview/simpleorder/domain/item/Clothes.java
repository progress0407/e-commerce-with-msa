package codereview.simpleorder.domain.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clothes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clothes_id")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int availableQuantity;

    public Clothes(String name, String size, int price, int availableQuantity) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }
}
