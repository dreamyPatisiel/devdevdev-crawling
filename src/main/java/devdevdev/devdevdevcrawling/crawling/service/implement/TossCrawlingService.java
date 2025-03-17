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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossCrawlingService implements CrawlingService {

    private Long companyId;
    private String companyName;
    private WebDriver webDriver;
    private WebDriverWait wait;
    private final static String techBlogUrl = "https://toss.tech/tech";

    public List<CrawledTechArticleDto> crawlAllTechBlogs(Company company) {

        log.info("Toss 기술블로그 크롤링 시작");

        webDriver = new ChromeDriver();

        companyId = company.getId();
        companyName = company.getName();

        List<CrawledTechArticleDto> techArticles = new ArrayList<>();

        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        for (int i = 1; i <= 1; i++) {
            // 페이지 이동
            String pagedTechBlogUrl = techBlogUrl + "?categories=tech&page=" + i;

            webDriver.get(pagedTechBlogUrl);

            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            List<WebElement> postElements = webDriver.findElements(By.className("e1sck7qg4"));

            log.info("...post list 가져오기 완료");

            // WebElement 마다 게시글 정보 가져오기
            for (WebElement post : postElements) {
                CrawledTechArticleDto techArticle = crawlPost(post);
//            if(techArticle.getRegDate().isBefore(LocalDate.of(2024, 12, 10))) {
//                break;
//            }
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

//            WebElement thumbnailImage = wait.until(driver -> post.findElement(By.cssSelector("img[alt='thumbnail']")));
//            wait.until(ExpectedConditions.attributeToBeNotEmpty(thumbnailImage, "src")); // src 속성이 비어있지 않게 기다림
//            String thumbnailUrl = thumbnailImage.getAttribute("src");
////            String thumbnailUrl = post.findElement(By.tagName("img")).getAttribute("srcset");
////            String thumbnailUrl = post.findElement(By.cssSelector("img[alt='thumbnail']")).getAttribute("src");

            // 썸네일 이미지 요소 찾기
            WebElement thumbnailImage = post.findElement(By.cssSelector("img[alt='thumbnail']"));

            // src 속성이 비어 있지 않을 때까지 대기
            wait.until(ExpectedConditions.attributeToBeNotEmpty(thumbnailImage, "src"));

            String thumbnailUrl = thumbnailImage.getAttribute("src");

            // base64 데이터가 나올 경우, 일정 시간 기다린 후 다시 가져오기
            if (thumbnailUrl.startsWith("data:image")) {
                Thread.sleep(2000); // 2초 대기 후 다시 가져오기
                thumbnailUrl = thumbnailImage.getAttribute("src");
            }


            String title = post.findElement(By.className("typography--h6")).getText();
            String description = post.findElement(By.className("typography--p")).getText();

            String dateString = post.findElement(By.className("typography--small")).getText();
            String[] split = dateString.split("·");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
            LocalDate regDate = LocalDate.parse(split[0].trim(), formatter);
            String author = split[1].trim();

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

        List<WebElement> contentsList = webDriver.findElements(By.className("css-1kxrhf3"));
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