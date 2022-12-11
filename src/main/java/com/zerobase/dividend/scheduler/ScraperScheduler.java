package com.zerobase.dividend.scheduler;

import com.zerobase.dividend.model.Company;
import com.zerobase.dividend.model.ScrapedResult;
import com.zerobase.dividend.model.constants.CacheKey;
import com.zerobase.dividend.persist.CompanyRepository;
import com.zerobase.dividend.persist.DividendRepository;
import com.zerobase.dividend.persist.entity.CompanyEntity;
import com.zerobase.dividend.persist.entity.DividendEntity;
import com.zerobase.dividend.scraper.Scraper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final Scraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true) //캐시 삭제
    @Scheduled(cron = "${scheduler.scrap.yahoo}") //매일 정각
    public void yahooFinanceScheduling() {
        log.info("scraping scheduler is started");
        //저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        //회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
                new Company(company.getTicker(), company.getName()));
            //스크래핑 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                //디비든 모델을 디비든 엔티티로 매핑
                .map(e -> new DividendEntity(company.getId(), e))
                //엘리먼트를 하나씩 디비든 레퍼지토리에 삽입
                .forEach(e -> {
                    boolean exists = this.dividendRepository.existsByCompanyIdAndDate(
                        e.getCompanyId(), e.getDate());
                    if (!exists) {
                        this.dividendRepository.save(e);
                    }
                });

            //연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000); // 3초
            } catch (InterruptedException e) { //인터럽트를 받는 스레드가 blocking 될 수 있는 메소드를 실행할때 발생
                Thread.currentThread().interrupt();
            }
        }


    }
}
