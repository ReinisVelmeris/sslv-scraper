package com.scraper.sslv_scraper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scraper.sslv_scraper.model.Advertisement;
import com.scraper.sslv_scraper.service.ScraperService;



@RestController
public class ScraperController {
    @Autowired
    private ScraperService scraperService;

    @GetMapping("/scrape")
    public ResponseEntity<List<Advertisement>> scrape() {
        try{
            List<Advertisement> ads = scraperService.scrapeAllAdvertisements();
            return ResponseEntity.ok(ads);
        }catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/links")
    public List<String> getAvailableLinks() {
        return scraperService.getAllRealEstateLinks();
    }
    
    
}