package devdevdev.devdevdevcrawling.crawling.dto;

import lombok.Data;

@Data
public class PublishTechArticle {
    private Long id;

    public PublishTechArticle(Long id) {
        this.id = id;
    }
}
