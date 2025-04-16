package devdevdev.devdevdevcrawling.crawling.service.implement;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.crawling.service.CrawlingService;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketCurlyCrawlingService implements CrawlingService {

    private WebDriver webDriver;
    private static Long companyId;
    private static String companyName;
    private final static String techBlogUrl = "https://helloworld.kurly.com/";

    public List<CrawledTechArticleDto> crawlAllTechBlogs(Company company) {

        log.info("마켓컬리 기술블로그 크롤링 시작");

        webDriver = new ChromeDriver();

        companyId = company.getId();
        companyName = company.getName();

        List<CrawledTechArticleDto> techArticles = new ArrayList<>();

        // 무한스크롤링
        webDriver.get(techBlogUrl);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        List<WebElement> postElements = webDriver.findElements(By.className("post-card"));

        log.info("...post list 가져오기 완료");

        // WebElement 마다 게시글 정보 가져오기
        for (WebElement post : postElements) {
            CrawledTechArticleDto techArticle = crawlPost(post);
//            if(techArticle.getRegDate().isBefore(LocalDate.of(2024, 12, 11))) {
//                break;
//            }
            techArticles.add(techArticle);
        }

        // 각 게시글마다 url 접근하여 contents 가져오기
        for (CrawledTechArticleDto techArticle : techArticles) {
            String contents = crawlPostContents(techArticle.getTechArticleUrl());
            techArticle.setContents(contents);
        }

        webDriver.quit();

        return techArticles;
    }

    @Override
    public List<CrawledTechArticleDto> crawlNewestTechBlogs(Company company) {
        return List.of();
    }

    @Override
    public CrawledTechArticleDto crawlPost(WebElement post) {
        try {
            String techArticleUrl = post.findElement(By.className("post-link")).getAttribute("href");
            String thumbnailUrl = null;
            String title = post.findElement(By.className("post-title")).getText();
            String description = null;

            String dateString = post.findElement(By.className("post-date")).getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
            LocalDate regDate = LocalDate.parse(dateString, formatter);
            String author = post.findElement(By.className("post-autor")).getText();

            return CrawledTechArticleDto.builder()
                    .title(title)
                    .regDate(regDate)
                    .techArticleUrl(techArticleUrl)
                    .description(description)
                    .thumbnailUrl(thumbnailUrl)
                    .author(author)
                    .company(companyName)
                    .companyId(companyId)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public List<CrawledTechArticleDto> crawlPosts(String postUrl) {
        return List.of();
    }

    @Override
    public String crawlPostContents(String postUrl) {

        webDriver.get(postUrl);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        List<WebElement> contentsList = webDriver.findElement(By.className("page-content"))
                .findElements(By.xpath("//p"));
        StringBuilder sb = new StringBuilder();

        for (WebElement content : contentsList) {
            try {
                sb.append(content.getText());
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }

        return sb.toString();
    }
}