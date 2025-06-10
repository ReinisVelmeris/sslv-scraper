package com.scraper.sslv_scraper.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scraper.sslv_scraper.dto.GraphStatsDTO;
import com.scraper.sslv_scraper.dto.RegionStatsDTO;
import com.scraper.sslv_scraper.repository.AdvertisementRepository;

@Service
public class AdvertisementService {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    public List<RegionStatsDTO> getAllRegionStats(String city) {
        return advertisementRepository.getAllRegionStats(city);
    }

    public List<RegionStatsDTO> getLatviaStats(){
        return advertisementRepository.getLatviaStats();
    }

    public List <GraphStatsDTO> getGraphStats(Integer year, Integer month, String city, String district){
        if (year == null && month == null) 
            return advertisementRepository.getStatsByYears(city, district);
        else if(year != null && month == null) 
            return advertisementRepository.getStatsByYearAndMonths(year, city, district);
        else 
            return advertisementRepository.getStatsByYearMonthAndDays(year, month, city, district);
    }
}
