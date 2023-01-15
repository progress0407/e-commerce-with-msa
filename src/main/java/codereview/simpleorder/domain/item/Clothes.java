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
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = true)
    private String size;

    @Column(nullable = true)
    private int price;

    public Clothes(String name, String size) {
        this.name = name;
        this.size = size;
    }
}
