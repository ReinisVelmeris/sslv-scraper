package com.scraper.sslv_scraper.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;

public class DynamicFilterRequest {
    @NotNull
    private Map<String, Object> filters;

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
