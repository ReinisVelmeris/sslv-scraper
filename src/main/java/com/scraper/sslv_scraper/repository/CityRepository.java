package com.scraper.sslv_scraper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scraper.sslv_scraper.model.City;

public interface CityRepository extends BaseRepository<City, Long> {
    City findByName(String name);
    City findByUrlPath(String urlPath);
    Optional<City> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
