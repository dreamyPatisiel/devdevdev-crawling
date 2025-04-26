package devdevdev.devdevdevcrawling.domain.service;

import devdevdev.devdevdevcrawling.domain.entity.TechArticle;
import devdevdev.devdevdevcrawling.domain.repository.TechArticleRepository;
import devdevdev.devdevdevcrawling.domain.response.TechArticleResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechArticleService {
    private final TechArticleRepository techArticleRepository;

    public List<TechArticleResponse> findAll() {
        List<TechArticle> techArticles = techArticleRepository.findAll();

        return techArticles.stream()
                .map(TechArticleResponse::create)
                .collect(Collectors.toList());
    }

    public List<TechArticle> saveAll(List<TechArticle> techArticles) {
        return techArticleRepository.saveAll(techArticles);
    }
}
