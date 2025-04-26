package devdevdev.devdevdevcrawling.crawling.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import devdevdev.devdevdevcrawling.crawling.dto.PublishTechArticleRequest;
import devdevdev.devdevdevcrawling.domain.entity.ApiKey;
import devdevdev.devdevdevcrawling.domain.repository.ApiKeyRepository;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @MockBean
    NotificationService notificationService;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Value("${devdevdev.api.domain}")
    private String domain;

    @Value("${service-name}")
    private String serviceName;

    @Test
    @DisplayName("기술블로그 글 발행 알림을 전송한다.")
    void sendNotifications() {
        // given
        ApiKey apiKey = ApiKey.builder()
                .serviceName("test-service")
                .apiKey("test-key")
                .build();
        apiKeyRepository.save(apiKey);

        PublishTechArticleRequest request = PublishTechArticleRequest.create(1L, Set.of((1L)));
        RestTemplate restTemplate = mock(RestTemplate.class);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("service-name", serviceName);
        httpHeaders.add("api-key", apiKey.getApiKey());

        // when
        doNothing().when(notificationService).sendNotifications(request);

        assertThatCode(() -> notificationService.sendNotifications(request))
                .doesNotThrowAnyException();

        verify(restTemplate).exchange(domain + "/devdevdev/api/v1/notifications/SUBSCRIPTION",
                HttpMethod.POST,
                new HttpEntity<>(request, httpHeaders),
                Void.class);
    }
}