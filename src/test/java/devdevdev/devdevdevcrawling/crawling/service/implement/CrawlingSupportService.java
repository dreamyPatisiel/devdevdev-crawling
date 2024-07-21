package devdevdev.devdevdevcrawling.crawling.service.implement;

import devdevdev.devdevdevcrawling.domain.entity.Company;
import devdevdev.devdevdevcrawling.domain.repository.CompanyRepository;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class CrawlingSupportService {

    @Autowired
    CompanyRepository companyRepository;

    @BeforeAll
    void setUp() {
        Company toss = createCompany("https://toss.im/career/jobs", "Toss", "https://toss.im/");
        Company baemin = createCompany("https://career.woowahan.com", "우아한 형제들", "https://www.woowahan.com/");
        Company aws = createCompany("https://aws.amazon.com/ko/careers", "AWS", "https://aws.amazon.com/ko/blogs/tech");
        Company line = createCompany("https://careers.linecorp.com/ko/jobs", "LINE", "https://line.me/ko/");
        Company naver = createCompany("https://recruit.navercorp.com/rcrt/list.do", "NAVER", "https://m.naver.com/");
        Company socar = createCompany("https://www.socarcorp.kr/careers/jobs", "쏘카", "https://www.socar.kr/");
        Company marketKurly = createCompany("https://kurly.career.greetinghr.com/", "마켓컬리", "https://www.kurly.com/main");
        Company kakaoPay = createCompany("https://kakaopay.career.greetinghr.com/", "카카오페이", "https://www.kakaopay.com/");

        companyRepository.saveAll(List.of(toss, baemin, aws, line, naver, socar, marketKurly, kakaoPay));
    }

//    @AfterAll
//    void tearDown() {
//        companyRepository.deleteAllInBatch();
//    }

    private Company createCompany(String careerUrl, String name, String officialUrl) {
        return Company.builder()
                .careerUrl(careerUrl)
                .name(name)
                .officialUrl(officialUrl)
                .build();
    }

    public Company getCompany(String name) {
        return companyRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 회사가 없습니다."));
    }
}
