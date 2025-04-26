package devdevdev.devdevdevcrawling.crawling.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import devdevdev.devdevdevcrawling.crawling.dto.PublishTechArticleRequest;
import devdevdev.devdevdevcrawling.domain.entity.ApiKey;
import devdevdev.devdevdevcrawling.domain.repository.ApiKeyRepository;
import devdevdev.devdevdevcrawling.elastic.repository.ElasticTechArticleRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    ElasticTechArticleRepository elasticTechArticleRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Value("${devdevdev.api.domain}")
    private String domain;

    @Value("${service-name}")
    private String serviceName;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        // RestTemplate은 진짜 빈을 써야 MockRestServiceServer로 검증 가능
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    @DisplayName("기술블로그 글 발행 알림을 전송한다.")
    void sendNotifications() {
        // given
        ApiKey apiKey = ApiKey.builder()
                .serviceName(serviceName)
                .apiKey("test-key")
                .build();
        apiKeyRepository.save(apiKey);

        PublishTechArticleRequest request = PublishTechArticleRequest.create(1L, Set.of(1L));

        mockServer.expect(requestTo(domain + "/devdevdev/api/v1/notifications/SUBSCRIPTION"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("service-name", serviceName))
                .andExpect(header("api-key", "test-key"))
                .andRespond(withSuccess());

        // when // then
        assertThatCode(() -> notificationService.sendNotifications(request))
                .doesNotThrowAnyException();

        // MockRestServiceServer 검증
        mockServer.verify();
    }

    @Test
    @DisplayName("기술블로그 글 발행 알림을 전송할 때 예외가 발생한다.")
    void sendNotificationsFail() {
        // given
        ApiKey apiKey = ApiKey.builder()
                .serviceName(serviceName)
                .apiKey("test-key")
                .build();
        apiKeyRepository.save(apiKey);

        PublishTechArticleRequest request = PublishTechArticleRequest.create(1L, Set.of(1L));

        mockServer.expect(requestTo(domain + "/devdevdev/api/v1/notifications/SUBSCRIPTION"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("service-name", serviceName))
                .andExpect(header("api-key", "test-key"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // when // then
        assertThatThrownBy(() -> notificationService.sendNotifications(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notification failed");

        mockServer.verify();
    }

}