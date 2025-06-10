package com.scraper.sslv_scraper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scraper.sslv_scraper.dto.GraphStatsDTO;
import com.scraper.sslv_scraper.dto.RegionStatsDTO;
import com.scraper.sslv_scraper.model.Advertisement;

public interface AdvertisementRepository
        extends BaseRepository<Advertisement, Long>, JpaSpecificationExecutor<Advertisement> {
    Optional<Advertisement> findByUrl(String url);

    @Query("SELECT new com.scraper.sslv_scraper.dto.RegionStatsDTO(" +
            "c.name, c.slug, COUNT(a.id), " +
            "COALESCE(ROUND(AVG(a.price), 2), 0), " +
            "COALESCE(ROUND(AVG(a.pricePerSquareMeter), 2), 0)) " +
            "FROM Advertisement a " +
            "JOIN a.apartment ap " +
            "JOIN ap.district d " +
            "RIGHT JOIN d.city c " +
            "WHERE (:city IS NULL OR c.slug = :city) " +
            "GROUP BY c.name, c.slug")
    List<RegionStatsDTO> getAllRegionStats(@Param("city") String citySlug);

    @Query("SELECT new com.scraper.sslv_scraper.dto.RegionStatsDTO(" +
            "'Latvia', 'latvia', COUNT(a.id), " +
            "COALESCE(ROUND(AVG(a.price), 2), 0), " +
            "COALESCE(ROUND(AVG(a.pricePerSquareMeter), 2), 0)) " +
            "FROM Advertisement a " +
            "JOIN a.apartment ap " +
            "JOIN ap.district d " +
            "JOIN d.city c")
    List<RegionStatsDTO> getLatviaStats();

    @Query(value = """
        SELECT
            EXTRACT(YEAR FROM a.posted_at) AS year,
            COUNT(*) AS total_ads,
            ROUND(AVG(CASE WHEN a.type = 'sale' THEN a.price ELSE NULL END), 2) AS avg_sale_price,
            ROUND(AVG(CASE WHEN a.type = 'rent' THEN a.price ELSE NULL END), 2) AS avg_rent_price,
            ROUND(AVG(CASE WHEN a.type = 'sale' THEN a.price_per_square_meter ELSE NULL END), 2) AS avg_sale_price_m2,
            ROUND(AVG(CASE WHEN a.type = 'rent' THEN a.price_per_square_meter ELSE NULL END), 2) AS avg_rent_price_m2
        FROM advertisements a
        JOIN apartments ap ON a.apartment_id = ap.id
        JOIN districts d ON ap.district_id = d.id
        JOIN cities c ON d.city_id = c.id
        WHERE
            (:city IS NULL OR c.slug = :city) AND
            (:district IS NULL OR d.slug = :district)
        GROUP BY EXTRACT(YEAR FROM a.posted_at)
        """, nativeQuery = true)
    List<GraphStatsDTO> getStatsByYears( @Param("city") String city, @Param("district") String district);

    @Query(value = """
        SELECT
            EXTRACT(YEAR FROM a.posted_at) AS year,
            EXTRACT(MONTH FROM a.posted_at) AS month,
            COUNT(*) AS total_ads,
            ROUND(AVG(CASE WHEN a.type = 'sale' THEN a.price ELSE NULL END), 2) AS avg_sale_price,
            ROUND(AVG(CASE WHEN a.type = 'rent' THEN a.price ELSE NULL END), 2) AS avg_rent_price,
            ROUND(AVG(CASE WHEN a.type = 'sale' THEN a.price_per_square_meter ELSE NULL END), 2) AS avg_sale_price_m2,
            ROUND(AVG(CASE WHEN a.type = 'rent' THEN a.price_per_square_meter ELSE NULL END), 2) AS avg_rent_price_m2
        FROM advertisements a
        JOIN apartments ap ON a.apartment_id = ap.id
        JOIN districts d ON ap.district_id = d.id
        JOIN cities c ON d.city_id = c.id
        WHERE 
            (:year IS NULL OR EXTRACT(YEAR FROM a.posted_at) = :year) AND
            (:city IS NULL OR c.slug = :city) AND
            (:district IS NULL OR d.slug = :district)
        GROUP BY EXTRACT(YEAR FROM a.posted_at), EXTRACT(MONTH FROM a.posted_at)
        """, nativeQuery = true)
    List<GraphStatsDTO> getStatsByYearAndMonths(@Param("year") Integer year, @Param("city") String city, @Param("district") String district);

    @Query(value = """
        SELECT
            EXTRACT(YEAR FROM a.posted_at) AS year,
            EXTRACT(MONTH FROM a.posted_at) AS month,
            EXTRACT(DAY FROM a.posted_at) AS day,
            COUNT(*) AS total_ads,
            ROUND(AVG(CASE WHEN a.type = 'sale' THEN a.price ELSE NULL END), 2) AS avg_sale_price,
            ROUND(AVG(CASE WHEN a.type = 'rent' THEN a.price ELSE NULL END), 2) AS avg_rent_price,
            ROUND(AVG(CASE WHEN a.type = 'sale' THEN a.price_per_square_meter ELSE NULL END), 2) AS avg_sale_price_m2,
            ROUND(AVG(CASE WHEN a.type = 'rent' THEN a.price_per_square_meter ELSE NULL END), 2) AS avg_rent_price_m2
        FROM advertisements a
        JOIN apartments ap ON a.apartment_id = ap.id
        JOIN districts d ON ap.district_id = d.id
        JOIN cities c ON d.city_id = c.id
        WHERE 
            (:year IS NULL OR EXTRACT(YEAR FROM a.posted_at) = :year) AND
            (:month IS NULL OR EXTRACT(MONTH FROM a.posted_at) = :month) AND
            (:city IS NULL OR c.slug = :city) AND
            (:district IS NULL OR d.slug = :district)
        GROUP BY EXTRACT(YEAR FROM a.posted_at), EXTRACT(MONTH FROM a.posted_at), EXTRACT(DAY FROM a.posted_at)
        """, nativeQuery = true)
    List<GraphStatsDTO> getStatsByYearMonthAndDays(
        @Param("year") Integer year,
        @Param("month") Integer month,
        @Param("city") String city,
        @Param("district") String district);
}
