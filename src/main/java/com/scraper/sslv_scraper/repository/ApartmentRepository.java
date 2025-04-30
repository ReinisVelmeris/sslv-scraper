package com.scraper.sslv_scraper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scraper.sslv_scraper.model.Apartment;
import com.scraper.sslv_scraper.model.District;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
     Optional<Apartment> findByDistrictAndStreetAndRoomNoAndArea(District district, String street, String roomNo, double area);
}
