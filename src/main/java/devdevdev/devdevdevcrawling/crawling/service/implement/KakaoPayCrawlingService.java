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
public class KakaoPayCrawlingService implements CrawlingService {

    private WebDriver webDriver;
    private static Long companyId;
    private static String companyName;
    private final static String techBlogUrl = "https://tech.kakaopay.com/";

    public List<CrawledTechArticleDto> crawlAllTechBlogs(Company company) {
        log.info("카카오페이 기술블로그 크롤링 시작");

        webDriver = new ChromeDriver();

        companyId = company.getId();
        companyName = company.getName();

        List<CrawledTechArticleDto> techArticles = new ArrayList<>();

        // 1~26
        // 1~10, 11~20, 21~26
        for (int i = 21; i <= 26; i++) {
            // 페이지 이동
            String page = "/page/" + i;
            String pagedTechBlogUrl = techBlogUrl + page;

            log.info("...page " + i + " 가져오기: " + pagedTechBlogUrl);

            webDriver.get(pagedTechBlogUrl);
            webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

            List<WebElement> postElements = webDriver.findElements(By.className("_postListItem_1cl5f_66"));

            log.info("...page " + i + " 가져오기 완료");

            // WebElement 마다 게시글 정보 가져오기
            for (WebElement post : postElements) {
                CrawledTechArticleDto techArticle = crawlPost(post);
//                if(techArticle.getRegDate().isBefore(LocalDate.of(2024, 12, 11))) {
//                    break;
//                }
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
            String techArticleUrl = post.findElement(By.cssSelector("a")).getAttribute("href");
            String thumbnailUrl = post.findElement(By.cssSelector("picture img")).getAttribute("src");
            String title = post.findElement(By.cssSelector("strong")).getText();
            String description = post.findElement(By.cssSelector("p")).getText();

            String dateString = post.findElement(By.cssSelector("time")).getText();
            dateString = dateString.replaceAll("\\s+", "");  // 모든 공백 제거

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M.d");
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

        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(1300));

        List<WebElement> contentsList = webDriver.findElement(By.cssSelector(".content .post .markdown"))
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