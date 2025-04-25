package com.scraper.sslv_scraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scraper.sslv_scraper.model.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    
}
