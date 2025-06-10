package com.scraper.sslv_scraper.config;

import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.scraper.sslv_scraper.model.Advertisement;
import com.scraper.sslv_scraper.model.Apartment;
import com.scraper.sslv_scraper.model.City;
import com.scraper.sslv_scraper.model.District;

@Configuration
public class EntityClassRegistry {
    private final Map<String, Class<?>> registry = Map.of(
        "advertisement", Advertisement.class,
        "city", City.class,
        "district", District.class,
        "apartment", Apartment.class
    );

    public Class<?> getEntityClass(String name) {
        Class<?> class1 = registry.get(name);
        if(class1 == null ) throw new IllegalArgumentException("Unknown entity: "+ name);
        return class1;
    }
}
