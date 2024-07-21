package devdevdev.devdevdevcrawling.crawling.controller;

import devdevdev.devdevdevcrawling.crawling.service.MyCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawl")
@RequiredArgsConstructor
public class CrawlingController {

    private final MyCrawlingService myCrawlingService;

    @GetMapping("/{companyId}")
    public ResponseEntity<Void> crawlByCompany(@PathVariable Long companyId) throws Exception {

        myCrawlingService.crawlingByCompanyId(companyId);

        return ResponseEntity.ok(null);
    }
}
