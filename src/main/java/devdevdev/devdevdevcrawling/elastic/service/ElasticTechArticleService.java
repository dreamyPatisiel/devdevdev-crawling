package devdevdev.devdevdevcrawling.elastic.service;

import com.google.common.collect.Lists;
import devdevdev.devdevdevcrawling.crawling.dto.CrawledTechArticleDto;
import devdevdev.devdevdevcrawling.elastic.document.ElasticTechArticle;
import devdevdev.devdevdevcrawling.elastic.repository.ElasticTechArticleRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticTechArticleService {

    private final ElasticTechArticleRepository elasticTechArticleRepository;

    public List<ElasticTechArticle> saveAllTechArticles(List<CrawledTechArticleDto> techArticles) {
        // techArticleDto -> ElasticTechArticle 변형
        List<ElasticTechArticle> elasticTechArticles = techArticles.stream()
                .map(ElasticTechArticle::from)
                .collect(Collectors.toList());

        // 저장 및 저장된 리스트 반환
        Iterable<ElasticTechArticle> savedElasticTechArticle = elasticTechArticleRepository.saveAll(elasticTechArticles);

        return Lists.newArrayList(savedElasticTechArticle);
    }
}
