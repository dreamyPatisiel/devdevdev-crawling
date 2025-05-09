package devdevdev.devdevdevcrawling.crawling.service;

import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.domain.entity.Company;
import devdevdev.devdevdevcrawling.domain.entity.TechArticle;
import devdevdev.devdevdevcrawling.domain.service.CompanyService;
import devdevdev.devdevdevcrawling.domain.service.TechArticleService;
import devdevdev.devdevdevcrawling.elastic.document.ElasticTechArticle;
import devdevdev.devdevdevcrawling.elastic.service.ElasticTechArticleService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyCrawlingService {

    private final CrawlingServiceStrategy crawlingServiceStrategy;
    private final CompanyService companyService;
    private final TechArticleService techArticleService;
    private final ElasticTechArticleService elasticTechArticleService;

    public Set<Long> crawlingByCompanyId(Long companyId) throws Exception {
        // company 찾기
        Company company = companyService.findByCompanyId(companyId)
                .orElseThrow(() -> new Exception("알맞은 회사를 찾을 수 없습니다."));

        // 기술블로그 크롤링
        CrawlingService crawlingService = crawlingServiceStrategy.getCrawlingService(companyId);
        List<CrawledTechArticleDto> crawledTechArticles = crawlingService.crawlAllTechBlogs(company);

        crawledTechArticles.size();

        // 엘라스틱서치에 기술블로그 저장
        List<ElasticTechArticle> savedElasticTechArticle = elasticTechArticleService.saveAllTechArticles(
                crawledTechArticles);
        savedElasticTechArticle.size();

        // RDS에 기술블로그 저장
        List<TechArticle> techArticles = savedElasticTechArticle.stream()
                .map(elasticTechArticle -> TechArticle.createTechArticle(elasticTechArticle, company))
                .toList();
        List<TechArticle> savedTechArticles = techArticleService.saveAll(techArticles);

        // 아이디 반환
        return savedTechArticles.stream()
                .map(TechArticle::getId)
                .collect(Collectors.toSet());
    }
}
