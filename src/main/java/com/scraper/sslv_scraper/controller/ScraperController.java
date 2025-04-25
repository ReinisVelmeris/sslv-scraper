package com.scraper.sslv_scraper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scraper.sslv_scraper.model.Advertisement;
import com.scraper.sslv_scraper.service.ScraperService;


@RestController
public class ScraperController {
    @Autowired
    private ScraperService scraperService;

    @GetMapping("/scrape")
    public List<Advertisement> scrape() {
        return scraperService.exctractData("riga");
    }
    
}