package devdevdev.devdevdevcrawling.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "official_url")
    private String officialUrl;

    @Column(name = "official_image_url")
    private String officialImageUrl;

    @Column(name = "career_url")
    private String careerUrl;

    @OneToMany(mappedBy = "company")
    private List<TechArticle> techArticles = new ArrayList<>();

    @Builder
    private Company(String name, String officialUrl, String officialImageUrl, String careerUrl) {
        this.name = name;
        this.officialUrl = officialUrl;
        this.officialImageUrl = officialImageUrl;
        this.careerUrl = careerUrl;
    }
}