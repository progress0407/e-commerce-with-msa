package codereview.simpleorder.repository.command;

import codereview.simpleorder.domain.item.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
}
