package com.scraper.sslv_scraper.repository;

import java.lang.classfile.ClassFile.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scraper.sslv_scraper.model.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<Advertisement> findByUrl(String url);
}
