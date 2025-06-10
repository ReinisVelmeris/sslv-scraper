package com.scraper.sslv_scraper.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scraper.sslv_scraper.dto.RegionStatsDTO;
import com.scraper.sslv_scraper.repository.AdvertisementRepository;
import com.scraper.sslv_scraper.service.AdvertisementService;

@RestController
@RequestMapping("/advertisements")
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;

    @Autowired
    private AdvertisementService advertisementService;

    AdvertisementController(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getAllStats(
            @RequestParam(required = false) String city
    ) {
        try {
            List<RegionStatsDTO> ads = null;
            if(city == null || city.isEmpty()){
                ads = advertisementService.getLatviaStats();
            }else{
                ads = advertisementService.getAllRegionStats(city);
            }

            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/stats/graph")
    public ResponseEntity<?> getGraphStats(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String district
    ) {
        try{
            return ResponseEntity.ok(advertisementService.getGraphStats(year, month, city, district));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
}
