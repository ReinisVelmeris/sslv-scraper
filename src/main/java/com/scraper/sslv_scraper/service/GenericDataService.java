package com.scraper.sslv_scraper.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import com.scraper.sslv_scraper.config.EntityClassRegistry;
import com.scraper.sslv_scraper.config.RepositoryRegistry;
import com.scraper.sslv_scraper.dto.DynamicFilterRequest;
import com.scraper.sslv_scraper.model.Advertisement;
import com.scraper.sslv_scraper.repository.AdvertisementRepository;
import com.scraper.sslv_scraper.specification.GenericFilterSpecification;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GenericDataService {
    
    private final EntityClassRegistry registry;
    private final RepositoryRegistry repositoryRegistry;
    private final GenericFilterSpecification<Object> specBuilder;

    public List<?> filter(String entityName, Map<String, Object> filters) {
        Class<?> entityClass = registry.getEntityClass(entityName);
        JpaSpecificationExecutor<Object> repo = (JpaSpecificationExecutor<Object>) repositoryRegistry.getRepository(entityClass);
        Specification<Object> spec = specBuilder.build(entityClass, filters);
        return repo.findAll(spec);
    }


}
