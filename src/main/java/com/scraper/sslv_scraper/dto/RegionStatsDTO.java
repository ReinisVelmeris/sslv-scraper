package com.scraper.sslv_scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionStatsDTO {
    private String regionName;
    private String regionSlug;
    private Long adCount;
    private Double avgPrice;
    private Double avgPricePerSquareMeter;
}
