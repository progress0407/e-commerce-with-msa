package codereview.simpleorder.item.repository;

import codereview.simpleorder.item.domain.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    List<Item> findByIdIn(Collection<Long> itemIds);

    List<Item> findAll();
}
