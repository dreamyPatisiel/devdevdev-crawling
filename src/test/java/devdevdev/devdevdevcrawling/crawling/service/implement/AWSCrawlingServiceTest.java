package devdevdev.devdevdevcrawling.crawling.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AWSCrawlingServiceTest extends CrawlingSupportService {

    @Autowired
    AWSCrawlingService awsCrawlingService;

    @Test
    @DisplayName("asw 기술 블로그 크롤링 테스트")
    void crawlAllTechBlogs() {
        // given
        Company aws = getCompany("AWS");

        // when
        List<CrawledTechArticleDto> crawledTechArticleDtos = awsCrawlingService.crawlAllTechBlogs(aws);

        // then
        assertNotNull(crawledTechArticleDtos);
    }

    @Test
    @DisplayName("asw 최신 기술 블로그 크롤링 테스트")
    void crawlNewestTechBlogs() {
        // given
        Company aws = getCompany("AWS");

        // when
        List<CrawledTechArticleDto> crawledTechArticleDtos = awsCrawlingService.crawlNewestTechBlogs(aws);

        // then
        assertThat(crawledTechArticleDtos).hasSize(10)
                .satisfies(article -> {
                    assertThat(article).isNotNull();
                    assertThat(article).isInstanceOf(CrawledTechArticleDto.class);
                });
    }
}