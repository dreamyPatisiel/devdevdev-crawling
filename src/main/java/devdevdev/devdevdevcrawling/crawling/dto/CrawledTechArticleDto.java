package devdevdev.devdevdevcrawling.crawling.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
public class CrawledTechArticleDto {
    private String title;
    private LocalDate regDate;
    private String contents;
    private String techArticleUrl;
    private String description;
    private String thumbnailUrl;
    private String author;
    private String company;
    private Long companyId;

    @Builder
    public CrawledTechArticleDto(String title, LocalDate regDate, String contents, String techArticleUrl,
                                 String description,
                                 String thumbnailUrl, String author, String company, Long companyId) {
        this.title = title;
        this.regDate = regDate;
        this.contents = contents;
        this.techArticleUrl = techArticleUrl;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.author = author;
        this.company = company;
        this.companyId = companyId;
    }

    public static CrawledTechArticleDto of(String title, LocalDate regDate, String techArticleUrl,
                                           String description, String thumbnailUrl, String author, String company,
                                           Long companyId) {
        return CrawledTechArticleDto.builder()
                .title(title)
                .regDate(regDate)
                .techArticleUrl(techArticleUrl)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .author(author)
                .company(company)
                .companyId(companyId)
                .build();

    }

    public void changeContents(String contents) {
        this.contents = contents;
    }
}