package devdevdev.devdevdevcrawling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"devdevdev.devdevdevcrawling.domain.repository"})
public class DevdevdevCrawlingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevdevdevCrawlingApplication.class, args);
    }

}
