package codereview.simpleorder.repository.query;

import codereview.simpleorder.domain.item.Clothes;
import codereview.simpleorder.dto.item.ClothesResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClothesQueryRepository {

    private final EntityManager em;

    public List<ClothesResponse> findAll() {

        String query = "select new codereview.simpleorder.dto.item.ClothesResponse(c) from Clothes c";

        return em.createQuery(query, ClothesResponse.class)
                .getResultList();
    }
}
