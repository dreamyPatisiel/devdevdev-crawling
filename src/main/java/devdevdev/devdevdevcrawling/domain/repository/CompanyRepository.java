package devdevdev.devdevdevcrawling.domain.repository;

import devdevdev.devdevdevcrawling.domain.entity.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
}
