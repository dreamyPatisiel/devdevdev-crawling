package devdevdev.devdevdevcrawling.crawling.service;

import devdevdev.devdevdevcrawling.crawling.service.implement.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingServiceStrategy {

    private final ApplicationContext applicationContext;

    public CrawlingService getCrawlingService(Long companyId) {
        return switch (companyId.intValue()) {
            case 1 -> applicationContext.getBean(TossCrawlingService.class);
            case 2 -> applicationContext.getBean(WoowahanCrawlingService.class);
            case 3 -> applicationContext.getBean(AWSCrawlingService.class);
            case 4 -> applicationContext.getBean(Line2CrawlingService.class);
            case 5 -> applicationContext.getBean(NaverCrawlingService.class);
            case 6 -> applicationContext.getBean(SocarCrawlingService.class);
            case 7 -> applicationContext.getBean(MarketCurlyCrawlingService.class);
            case 8 -> applicationContext.getBean(KakaoPayCrawlingService.class);
            default -> null;
        };
    }
}
