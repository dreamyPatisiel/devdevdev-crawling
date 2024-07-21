package devdevdev.devdevdevcrawling.domain.service;

import devdevdev.devdevdevcrawling.domain.entity.Company;
import devdevdev.devdevdevcrawling.domain.repository.CompanyRepository;
import devdevdev.devdevdevcrawling.domain.response.CompanyResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Optional<Company> findByCompanyId(Long companyId) {
        return companyRepository.findById(companyId);
    }

    public List<CompanyResponse> findAll() {

        List<Company> companies = companyRepository.findAll();

        return companies.stream()
                .map(CompanyResponse::create)
                .collect(Collectors.toList());

    }

}
