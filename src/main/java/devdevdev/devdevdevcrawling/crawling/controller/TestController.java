package devdevdev.devdevdevcrawling.crawling.controller;

import devdevdev.devdevdevcrawling.domain.response.CompanyResponse;
import devdevdev.devdevdevcrawling.domain.response.TechArticleResponse;
import devdevdev.devdevdevcrawling.domain.service.CompanyService;
import devdevdev.devdevdevcrawling.domain.service.TechArticleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final CompanyService companyService;
    private final TechArticleService techArticleService;

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyResponse>> findAllCompanies() {

        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/articles")
    public ResponseEntity<List<TechArticleResponse>> findAllTechArticles() {

        return ResponseEntity.ok(techArticleService.findAll());
    }
}
