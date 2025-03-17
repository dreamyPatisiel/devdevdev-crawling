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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechArticle extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String title;

    @Column(name = "comment_total_count")
    private Long commentTotalCount;

    @Column(name = "popular_score")
    private Long popularScore;

    @Column(name = "recommend_total_count")
    private Long recommendTotalCount;

    @Column(name = "view_total_count")
    private Long viewTotalCount;

    @Column(name = "tech_article_url", length = 255)
    private String techArticleUrl;

    @Column(name = "author")
    private String author;

    @Column(name = "reg_date")
    private LocalDate regDate;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;

    @Column(name = "elastic_id", length = 255)
    private String elasticId;


    @Builder
    public TechArticle(Company company, String title, Long commentTotalCount, Long popularScore,
                       Long recommendTotalCount,
                       Long viewTotalCount, String techArticleUrl, String author, LocalDate regDate,
                       String thumbnailUrl,
                       String elasticId) {
        this.company = company;
        this.title = title;
        this.elasticId = elasticId;
        this.techArticleUrl = techArticleUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.author = author;
        this.regDate = regDate;
        this.commentTotalCount = commentTotalCount;
        this.popularScore = popularScore;
        this.recommendTotalCount = recommendTotalCount;
        this.viewTotalCount = viewTotalCount;
    }

    public static TechArticle createTechArticle(ElasticTechArticle elasticTechArticle, Company company) {
        return TechArticle.builder()
                .company(company)
                .title(elasticTechArticle.getTitle())
                .elasticId(elasticTechArticle.getId())
                .techArticleUrl(elasticTechArticle.getTechArticleUrl())
                .thumbnailUrl(elasticTechArticle.getThumbnailUrl())
                .author(elasticTechArticle.getAuthor())
                .regDate(elasticTechArticle.getRegDate())
                .viewTotalCount(0L)
                .recommendTotalCount(0L)
                .commentTotalCount(0L)
                .popularScore(0L)
                .build();
    }
}
