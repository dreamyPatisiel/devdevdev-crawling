package devdevdev.devdevdevcrawling.crawling.service.implement;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.crawling.service.CrawlingService;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Line2CrawlingService implements CrawlingService {

    private WebDriver webDriver;
    private static Long companyId;
    private static String companyName;
    private final static String techBlogUrl = "https://techblog.lycorp.co.jp/ko";

    public List<CrawledTechArticleDto> crawlAllTechBlogs(Company company) {
        log.info("라인 기술블로그 크롤링 시작");

        webDriver = new ChromeDriver();

        companyId = company.getId();
        companyName = company.getName();

        List<CrawledTechArticleDto> techArticles = new ArrayList<>();

        // 1~8
        for (int i = 5; i <= 8; i++) {
            // 페이지 이동
            String pagedTechBlogUrl = techBlogUrl;
            if (i >= 2) {
                String page = "/page/" + i;
                pagedTechBlogUrl = pagedTechBlogUrl + page;
            }

            log.info("...page " + i + " 가져오기");
            webDriver.get(pagedTechBlogUrl);
            webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

            List<WebElement> postElements = webDriver.findElements(By.cssSelector(".link.list_item"));

            log.info("...page " + i + " 가져오기 완료");

            // WebElement 마다 게시글 정보 가져오기
            for (WebElement post : postElements) {
                CrawledTechArticleDto techArticle = crawlPost(post);
                techArticles.add(techArticle);
            }
        }

        // 각 게시글마다 url 접근하여 contents 가져오기
        for (CrawledTechArticleDto techArticle : techArticles) {
            if (techArticle == null || techArticle.getTechArticleUrl() == null) {
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
            String techArticleUrl = post.getAttribute("href");
            String thumbnailUrl = post.findElement(By.cssSelector(".thumbnail img")).getAttribute("src");
            String title = post.findElement(By.cssSelector(".title")).getText();
            String description = null;

            String dateString = post.findElement(By.cssSelector(".update")).getText().replace("Date:", "").trim(); // "2025.03.19"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            LocalDate regDate = LocalDate.parse(dateString, formatter);

            String author = null;

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

        WebElement contentsElement = webDriver.findElement(By.className("post_content_wrap"));

        List<WebElement> contentsList = contentsElement.findElements(By.cssSelector("h1, h2, h3, h4, h5, h6, p"));
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