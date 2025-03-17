package devdevdev.devdevdevcrawling.domain.repository;

import devdevdev.devdevdevcrawling.domain.entity.TechArticle;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechArticleRepository extends JpaRepository<TechArticle, Long> {
    Optional<TechArticle> findByElasticId(String elasticId);
}
