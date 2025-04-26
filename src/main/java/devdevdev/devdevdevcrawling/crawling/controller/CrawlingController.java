package devdevdev.devdevdevcrawling.crawling.controller;

import devdevdev.devdevdevcrawling.crawling.dto.PublishTechArticleRequest;
import devdevdev.devdevdevcrawling.crawling.service.MyCrawlingService;
import devdevdev.devdevdevcrawling.crawling.service.NotificationService;
import java.util.Set;
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
    private final NotificationService notificationService;

    @GetMapping("/{companyId}")
    public ResponseEntity<Void> crawlByCompany(@PathVariable Long companyId) throws Exception {

        Set<Long> techArticleIds = myCrawlingService.crawlingByCompanyId(companyId);

        // 기술블로그 글 발행 알림을 전송한다.
        PublishTechArticleRequest request = PublishTechArticleRequest.create(companyId, techArticleIds);
        notificationService.sendNotifications(request);

        return ResponseEntity.ok(null);
    }
}
