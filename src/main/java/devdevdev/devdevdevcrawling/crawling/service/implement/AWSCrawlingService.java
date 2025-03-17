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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSCrawlingService implements CrawlingService {

    private Long companyId;
    private String companyName;
    private WebDriver webDriver;
    private final static String techBlogUrl = "https://aws.amazon.com/ko/blogs/tech";

    @Override
    public List<CrawledTechArticleDto> crawlAllTechBlogs(Company company) {

        log.info("AWS 기술블로그 크롤링 시작");

        webDriver = new ChromeDriver();

        companyId = company.getId();
        companyName = company.getName();

        List<CrawledTechArticleDto> techArticles = new ArrayList<>();

//        for (int i = 1; i <= 44; i++) {
        for (int i = 1; i <= 5; i++) {
            // 페이지 이동
            String pagedTechBlogUrl = techBlogUrl;
            if (i >= 2) {
                String page = "/page/" + i;
                pagedTechBlogUrl = pagedTechBlogUrl + page;
            }

            log.info("...page " + i + " 가져오기: " + pagedTechBlogUrl);

            webDriver.get(pagedTechBlogUrl);
            webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

            List<WebElement> postElements = webDriver.findElements(By.className("blog-post"));

            log.info("...page " + i + " 가져오기 완료");

            // WebElement 마다 게시글 정보 가져오기
            for (WebElement post : postElements) {
                CrawledTechArticleDto techArticle = crawlPost(post);
//                if(techArticle.getRegDate().isBefore(LocalDate.of(2024, 12, 10))) {
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

    /**
     * @param company
     * @Note: 가장 최신의 AWS 기술블로그를 크롤링 한다.
     */
    @Override
    public List<CrawledTechArticleDto> crawlNewestTechBlogs(Company company) {

        companyId = company.getId();
        companyName = company.getName();

        // 크롬 드라이버 생성
        webDriver = new ChromeDriver();

        // 기술블로그 메인 페이지 크롤링
        List<CrawledTechArticleDto> crawledTechArticles = crawlPosts(techBlogUrl);

        // 기술블로그 본문 크롤링후 저장
        addCrawledPostsContentsTo(crawledTechArticles);

        webDriver.quit();

        return crawledTechArticles;
    }

    private void addCrawledPostsContentsTo(List<CrawledTechArticleDto> crawledTechArticles) {
        for (CrawledTechArticleDto crawledTechArticle : crawledTechArticles) {
            // url이 없는 게시글은 제외
            if (!ObjectUtils.isEmpty(crawledTechArticle) && StringUtils.hasText(
                    crawledTechArticle.getTechArticleUrl())) {
                // 각 게시글마다 url 접근하여 contents 가져오기
                String contents = crawlPostContents(crawledTechArticle.getTechArticleUrl());
                crawledTechArticle.changeContents(contents);
            }
        }
    }


    @Override
    public CrawledTechArticleDto crawlPost(WebElement post) {

        String techArticleUrl = post.findElement(By.cssSelector("a[property='url']")).getAttribute("href");
        String thumbnailUrl = post.findElement(By.className("wp-post-image")).getAttribute("src");
        String title = post.findElement(By.className("blog-post-title")).getText();
        String description = post.findElement(By.className("blog-post-excerpt")).getText();

        String dateString = post.findElement(By.tagName("time")).getAttribute("datetime");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDate regDate = LocalDate.parse(dateString, formatter);

        String author = post.findElement(By.cssSelector("span[property='author'] > span[property='name']"))
                .getText();

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
    }

    @Override
    public List<CrawledTechArticleDto> crawlPosts(String postUrl) {
        // 페이지 이동
        webDriver.get(postUrl);

        // 대기
        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        // 기술블로그 메인 페이지에서 게시글 리스트 가져오기
        List<WebElement> blogPosts = webDriver.findElements(By.className("blog-post"));

        List<CrawledTechArticleDto> crawledTechArticles = new ArrayList<>();
        for (WebElement blogPost : blogPosts) {
            crawledTechArticles.add(getCrawledTechArticleDtoBy(blogPost));
        }

        return crawledTechArticles;
    }

    private CrawledTechArticleDto getCrawledTechArticleDtoBy(WebElement blogPost) {
        String techArticleUrl = blogPost.findElement(By.cssSelector("a[property='url']")).getAttribute("href");
        String thumbnailUrl = blogPost.findElement(By.className("wp-post-image")).getAttribute("src");
        String title = blogPost.findElement(By.className("blog-post-title")).getText();
        String description = blogPost.findElement(By.className("blog-post-excerpt")).getText();
        String dateString = blogPost.findElement(By.tagName("time")).getAttribute("datetime");
        String author = blogPost.findElement(By.cssSelector("span[property='author'] > span[property='name']"))
                .getText();

        LocalDate regDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

        return CrawledTechArticleDto.of(title, regDate, techArticleUrl,
                description, thumbnailUrl, author, companyName, companyId);
    }

    @Override
    public String crawlPostContents(String postUrl) {

        webDriver.get(postUrl);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        List<WebElement> contentsList = webDriver.findElements(By.xpath("//p|//h1|//h2|//h3|//h4"));
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