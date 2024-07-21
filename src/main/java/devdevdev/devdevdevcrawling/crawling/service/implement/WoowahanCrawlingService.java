package devdevdev.devdevdevcrawling.crawling.service.implement;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.crawling.service.CrawlingService;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
public class WoowahanCrawlingService implements CrawlingService {

    private WebDriver webDriver;
    private static Long companyId;
    private static String companyName;
    private static String techBlogUrl;

    public List<CrawledTechArticleDto> crawlAllTechBlogs(Company company) {
        companyId = company.getId();
        companyName = company.getName();
        techBlogUrl = "https://techblog.woowahan.com";

        log.info("우아한형제들 기술블로그 크롤링 시작");

        System.setProperty("webdriver.chrome.driver", "/Users/soyoung/DevSpace/chromedriver-mac-x64/chromedriver");
        webDriver = new ChromeDriver();

        List<CrawledTechArticleDto> techArticles = new ArrayList<>();

        for (int i = 1; i <= 42; i++) {
            // 페이지 이동
            String page = "?paged=" + i;
            String pagedTechBlogUrl = techBlogUrl + page;

            log.info("...page " + i + " 가져오기: " + pagedTechBlogUrl);

            webDriver.get(pagedTechBlogUrl);
            webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

            List<WebElement> postElements = webDriver.findElements(By.className("post-item"));

            log.info("...page " + i + " 가져오기 완료");

            // WebElement 마다 게시글 정보 가져오기

            for (WebElement post : postElements) {

                // "firstpaint" 클래스가 아닌 요소만 필터링
                if (post.getAttribute("class").contains("firstpaint")) {
                    continue;
                }

                CrawledTechArticleDto techArticle = crawlPost(post);
                techArticles.add(techArticle);
            }

        }

        // 각 게시글마다 url 접근하여 contents 가져오기
        for (CrawledTechArticleDto techArticle : techArticles) {
            if (techArticle.getTechArticleUrl() == null) {
                continue;
            }
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
            String techArticleUrl = post.findElement(By.cssSelector(".post-item > a")).getAttribute("href");
            String thumbnailUrl = null;
            String title = post.findElement(By.className("post-title")).getText();
            String description = post.findElement(By.className("post-excerpt")).getText();

            String dateString = post.findElement(By.className("post-author-date")).getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM.dd.yyyy", Locale.ENGLISH);
            LocalDate regDate = LocalDate.parse(dateString, formatter);

            String author = post.findElement(By.className("post-author-name")).getText().trim();

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

        List<WebElement> contentsList = webDriver.findElement(By.className("post-content-body"))
                .findElements(By.xpath("//p|//h1|//h2|//h3|//h4"));
        StringBuilder sb = new StringBuilder();

        for (WebElement content : contentsList) {
            try {
                sb.append(content.getText());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return sb.toString();
    }
}