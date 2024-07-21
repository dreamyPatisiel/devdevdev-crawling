package devdevdev.devdevdevcrawling.crawling.service;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface CrawlingService {

    List<CrawledTechArticleDto> crawlTechBlogs(Company company) throws InterruptedException;
    CrawledTechArticleDto crawlPost(WebElement post) throws InterruptedException;
    String crawlPostContents(String postUrl) throws InterruptedException;
}
