package devdevdev.devdevdevcrawling.domain.repository;

import devdevdev.devdevdevcrawling.domain.entity.ApiKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByServiceName(String serviceName);
}
