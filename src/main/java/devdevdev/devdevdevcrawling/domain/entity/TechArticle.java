package devdevdev.devdevdevcrawling.domain.entity;

import devdevdev.devdevdevcrawling.elastic.document.ElasticTechArticle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechArticle extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "view_total_count")
    private Long viewTotalCount;

    @Column(name = "recommend_total_count")
    private Long recommendTotalCount;

    @Column(name = "comment_total_count")
    private Long commentTotalCount;

    @Column(name = "popular_score")
    private Long popularScore;

    @Column(name = "tech_article_url", length = 255)
    private String techArticleUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(length = 255)
    private String elasticId;

    @Builder
    private TechArticle(Long id, Long viewTotalCount, Long recommendTotalCount, Long commentTotalCount,
                       Long popularScore,
                       String techArticleUrl, Company company, String elasticId) {
        this.id = id;
        this.viewTotalCount = viewTotalCount;
        this.recommendTotalCount = recommendTotalCount;
        this.commentTotalCount = commentTotalCount;
        this.popularScore = popularScore;
        this.techArticleUrl = techArticleUrl;
        this.company = company;
        this.elasticId = elasticId;
    }

    public static TechArticle createTechArticle(ElasticTechArticle elasticTechArticle, Company company) {
        TechArticle techArticle = TechArticle.builder()
                .elasticId(elasticTechArticle.getId())
                .techArticleUrl(elasticTechArticle.getTechArticleUrl())
                .viewTotalCount(0L)
                .recommendTotalCount(0L)
                .commentTotalCount(0L)
                .popularScore(0L)
                .company(company)
                .build();

        techArticle.setCreatedAt(elasticTechArticle.getRegDate().atStartOfDay());

        return techArticle;
    }
}
