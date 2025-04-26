package devdevdev.devdevdevcrawling.crawling.service;

import devdevdev.devdevdevcrawling.crawling.dto.PublishTechArticleRequest;
import devdevdev.devdevdevcrawling.domain.entity.ApiKey;
import devdevdev.devdevdevcrawling.domain.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    @Value("${devdevdev.api.domain}")
    private String domain;

    @Value("${service-name}")
    private String serviceName;

    private final ApiKeyRepository apiKeyRepository;

    public void sendNotifications(PublishTechArticleRequest publishTechArticleRequest) {

        RestTemplate restTemplate = new RestTemplate();

        ApiKey findApiKey = apiKeyRepository.findByServiceName(serviceName).orElseThrow(() ->
                new RuntimeException("서비스 이름을 확인해주세요."));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("service-name", serviceName);
        httpHeaders.add("api-key", findApiKey.getApiKey());

        ResponseEntity<Void> response = restTemplate.exchange(
                domain + "/devdevdev/api/v1/notifications/SUBSCRIPTION",
                HttpMethod.POST,
                new HttpEntity<>(publishTechArticleRequest, httpHeaders),
                Void.class
        );

        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Notification failed");
        }
    }
}
