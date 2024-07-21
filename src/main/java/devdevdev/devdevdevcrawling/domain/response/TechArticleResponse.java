package devdevdev.devdevdevcrawling.domain.response;

import devdevdev.devdevdevcrawling.domain.entity.TechArticle;
import lombok.Builder;
import lombok.Data;

@Data
public class TechArticleResponse {
    private Long id;
    private String elasticId;
    private String techArticleUrl;

    @Builder
    public TechArticleResponse(Long id, String elasticId, String techArticleUrl) {
        this.id = id;
        this.elasticId = elasticId;
        this.techArticleUrl = techArticleUrl;
    }

    public static TechArticleResponse create(TechArticle techArticle) {
        return TechArticleResponse.builder()
                .id(techArticle.getId())
                .elasticId(techArticle.getElasticId())
                .techArticleUrl(techArticle.getTechArticleUrl())
                .build();
    }
}
