package com.scraper.sslv_scraper.config;

import java.util.Map;
import com.scraper.sslv_scraper.repository.DistrictRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.scraper.sslv_scraper.model.Advertisement;
import com.scraper.sslv_scraper.model.Apartment;
import com.scraper.sslv_scraper.model.City;
import com.scraper.sslv_scraper.model.District;
import com.scraper.sslv_scraper.repository.AdvertisementRepository;
import com.scraper.sslv_scraper.repository.ApartmentRepository;
import com.scraper.sslv_scraper.repository.CityRepository;

@Configuration
public class RepositoryRegistry {

    
    private final Map<Class<?>, JpaSpecificationExecutor<?>> repoMap;
    
    public RepositoryRegistry(
        AdvertisementRepository adRepo,
        CityRepository cityRepository,
        DistrictRepository districtRepository,
        ApartmentRepository apartmentRepository
        ){
        this.repoMap = Map.of(
            Advertisement.class, adRepo,
            City.class, cityRepository,
            District.class, districtRepository,
            Apartment.class, apartmentRepository

        );
    }

    public JpaSpecificationExecutor<?> getRepository(Class<?> entityClass){
        return repoMap.get(entityClass);
    }

}
