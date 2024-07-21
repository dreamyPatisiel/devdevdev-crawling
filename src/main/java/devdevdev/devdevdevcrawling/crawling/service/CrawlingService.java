package devdevdev.devdevdevcrawling.crawling.service;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface CrawlingService {
    List<CrawledTechArticleDto> crawlAllTechBlogs(Company company);
    List<CrawledTechArticleDto> crawlNewestTechBlogs(Company company);
    CrawledTechArticleDto crawlPost(WebElement post);
    List<CrawledTechArticleDto> crawlPosts(String postUrl);
    String crawlPostContents(String postContentsUrl);
}
