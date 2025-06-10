package com.scraper.sslv_scraper.dto;

public interface GraphStatsDTO {
    Integer getYear();
    Integer getMonth();
    Integer getDay();
    Integer getTotalAds();
    Double getAvgSalePrice();
    Double getAvgRentPrice();
    Double getAvgSalePriceM2();
    Double getAvgRentPriceM2();
}
