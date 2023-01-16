package codereview.simpleorder.repository.command;

import codereview.simpleorder.domain.item.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    List<Item> findByIdIn(List<Long> itemIds);
}
