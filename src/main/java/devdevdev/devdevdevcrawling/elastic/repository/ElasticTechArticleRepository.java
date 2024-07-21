package devdevdev.devdevdevcrawling.elastic.repository;

import devdevdev.devdevdevcrawling.elastic.document.ElasticTechArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticTechArticleRepository extends ElasticsearchRepository<ElasticTechArticle, String>, CrudRepository<ElasticTechArticle, String> {
}
