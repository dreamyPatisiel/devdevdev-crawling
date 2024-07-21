package devdevdev.devdevdevcrawling.crawling.service.implement;

import devdevdev.devdevdevcrawling.crawling.service.CrawlingService;
import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossCrawlingService implements CrawlingService {

    private WebDriver webDriver;
    private static Long companyId;
    private static String companyName;
    private static String techBlogUrl;

    public List<CrawledTechArticleDto> crawlTechBlogs(Company company) throws InterruptedException {
        companyId = company.getId();
        companyName = company.getName();
        techBlogUrl = "https://toss.tech/tech";
        log.info("Toss 기술블로그 크롤링 시작");

        System.setProperty("webdriver.chrome.driver", "/Users/soyoung/DevSpace/chromedriver-mac-x64/chromedriver");
        webDriver = new ChromeDriver();

        webDriver.get(techBlogUrl);
        Thread.sleep(1000);

        List<WebElement> postElements = webDriver.findElements(By.className("css-nq2ijc"));

        log.info("...post list 가져오기 완료");

        // WebElement 마다 게시글 정보 가져오기
        List<CrawledTechArticleDto> techArticles = new ArrayList<>();

        for (WebElement post : postElements) {
            CrawledTechArticleDto techArticle = crawlPost(post);
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
    public CrawledTechArticleDto crawlPost(WebElement post) throws InterruptedException {
        try {
            String techArticleUrl = post.getAttribute("href");
            String thumbnailUrl = post.findElement(By.tagName("img")).getAttribute("srcset");
            String title = post.findElement(By.className("typography--h6")).getText();
            String description = post.findElement(By.className("typography--p")).getText();

            String dateString = post.findElement(By.className("typography--small")).getText();
            String[] split = dateString.split(" · ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
            LocalDate regDate = LocalDate.parse(split[0], formatter);
            String author = split[1];

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
    public String crawlPostContents(String postUrl) throws InterruptedException {

        webDriver.get(postUrl);
        Thread.sleep(1000);

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