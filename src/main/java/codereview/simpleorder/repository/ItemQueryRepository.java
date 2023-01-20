package codereview.simpleorder.repository;

import codereview.simpleorder.dto.response.ItemResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final EntityManager em;

    public List<ItemResponse> findAll() {

        String query = "select new codereview.simpleorder.dto.response.ItemResponse(c) from Item c";

        return em.createQuery(query, ItemResponse.class)
                .getResultList();
    }
}
