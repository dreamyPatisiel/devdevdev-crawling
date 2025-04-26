package devdevdev.devdevdevcrawling.crawling.dto;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublishTechArticleRequest {

    private Long companyId;
    private Set<PublishTechArticle> techArticles;

    public PublishTechArticleRequest(Long companyId, Set<PublishTechArticle> techArticles) {
        this.companyId = companyId;
        this.techArticles = techArticles;
    }

    public static PublishTechArticleRequest create(Long companyId, Set<Long> techArticleIds) {
        Set<PublishTechArticle> publishTechArticles = techArticleIds.stream().map(PublishTechArticle::new)
                .collect(Collectors.toSet());

        return new PublishTechArticleRequest(companyId, publishTechArticles);
    }
}
