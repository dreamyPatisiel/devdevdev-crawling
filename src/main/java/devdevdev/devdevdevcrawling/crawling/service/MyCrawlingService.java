package devdevdev.devdevdevcrawling.crawling.service;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import devdevdev.devdevdevcrawling.domain.entity.TechArticle;
import devdevdev.devdevdevcrawling.domain.service.CompanyService;
import devdevdev.devdevdevcrawling.domain.service.TechArticleService;
import devdevdev.devdevdevcrawling.elastic.document.ElasticTechArticle;
import devdevdev.devdevdevcrawling.elastic.service.ElasticTechArticleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyCrawlingService {

    private final CrawlingServiceStrategy crawlingServiceStrategy;
    private final CompanyService companyService;
    private final TechArticleService techArticleService;
    private final ElasticTechArticleService elasticTechArticleService;

    public void crawlingByCompanyId(Long companyId) throws Exception {
        // company 찾기
        Company company = companyService.findByCompanyId(companyId)
                .orElseThrow(() -> new Exception("company가 없슴"));

        // 기술블로그 크롤링
        CrawlingService crawlingService = crawlingServiceStrategy.getCrawlingService(companyId);
        List<CrawledTechArticleDto> crawledTechArticles = crawlingService.crawlTechBlogs(company);

        // 엘라스틱서치에 기술블로그 저장
        List<ElasticTechArticle> savedElasticTechArticle = elasticTechArticleService.saveAllTechArticles(crawledTechArticles);

        // RDS에 기술블로그 저장
        List<TechArticle> techArticles = savedElasticTechArticle.stream()
                .map(elasticTechArticle -> TechArticle.createTechArticle(elasticTechArticle, company))
                .toList();
        List<TechArticle> savedTechArticles = techArticleService.saveAll(techArticles);

    }
}
