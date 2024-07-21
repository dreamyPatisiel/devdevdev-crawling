package devdevdev.devdevdevcrawling.domain.response;

import devdevdev.devdevdevcrawling.domain.entity.Company;
import lombok.Builder;
import lombok.Data;

@Data
public class CompanyResponse {
    private Long id;
    private String name;
    private String officialUrl;
    private String officialImageUrl;
    private String careerUrl;

    @Builder
    public CompanyResponse(Long id, String name, String officialUrl, String officialImageUrl, String careerUrl) {
        this.id = id;
        this.name = name;
        this.officialUrl = officialUrl;
        this.officialImageUrl = officialImageUrl;
        this.careerUrl = careerUrl;
    }

    public static CompanyResponse create(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .officialUrl(company.getOfficialUrl())
                .officialImageUrl(company.getOfficialImageUrl())
                .careerUrl(company.getCareerUrl())
                .build();
    }
}
