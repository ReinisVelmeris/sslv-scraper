package com.scraper.sslv_scraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scraper.sslv_scraper.model.City;
import com.scraper.sslv_scraper.model.District;
import java.util.List;
import java.util.Optional;


public interface DistrictRepository extends BaseRepository<District, Long> {
    District findByUrlPath(String urlPath);
    District findByName(String name);
    Optional<District> findBySlugAndCity(String slug, City city);
    boolean existsBySlugAndCity(String slug, City city);
}