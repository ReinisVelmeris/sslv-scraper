package com.scraper.sslv_scraper.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;

import com.scraper.sslv_scraper.model.Advertisement;

import lombok.AllArgsConstructor;

@SpringBootTest
@AllArgsConstructor
public class ScraperServiceTest {
    private final ScraperService scraperService;

    void testExtractDataReturnsEmptyListIfNoPages() {
        List<Advertisement> result = scraperService.exctractData("https://www.ss.lv/real-estate/flats/riga/centre/");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
