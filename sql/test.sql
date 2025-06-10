select * from cities;
SELECT * from districts;
select count(*) from advertisements;
select * from advertisements;
select * from APARTMENTS;
select count(*) from apartments;
select * from districts;


SELECT c.name, d.name, count(*)
FROM advertisements a
JOIN apartments ap on ap.id = a.APARTMENT_ID
JOIN districts d on d.id = ap.district_id
JOIN cities c on c.id = d.city_id
GROUP BY c.name, d.name;

select * from cities;

SELECT * FROM advertisements order by url ASC;

SELECT * FROM advertisements ad
JOIN apartments ap on ap.id= ad.apartment_id
JOIN districts d on ap.district_id = d.id
JOIN cities c on c.id = d.city_id
WHERE d.URL_PATH like '%centre%' and ad.url is not null;

select district, floor, rooms, count(*) 
from apartments ap 
where (select count(*) from advertisements ad where ad.APARTMENT_ID = ap.id) > 1 
group by district, floor, rooms order by count(*) desc;

SELECT c.name, c.slug, count(a.id) as ad_count, coalesce(round(avg(a.price),2),0) as ad_avg_price, coalesce(round(avg(a.price_per_square_meter),2),0) as ad_avg_price_per_square_meter
 FROM Advertisements a
  JOIN apartments ap ON ap.id = a.apartment_id
   JOIN districts d ON d.id = ap.district_id
   Right JOIN cities 
   c ON c.id = d.city_id
    GROUP BY c.name, c.slug;


SELECT
    c.name AS city,
    d.name AS district,
    EXTRACT(YEAR FROM a.posted_at) AS year,
    EXTRACT(MONTH FROM a.posted_at) AS month,
    COUNT(*) AS total_ads,
    ROUND(AVG(a.price), 2) AS avg_price,
    ROUND(AVG(a.price_per_square_meter), 2) AS avg_price_m2
FROM advertisements a
JOIN apartments ap ON a.apartment_id = ap.id
JOIN districts d ON ap.district_id = d.id
JOIN cities c ON d.city_id = c.id
GROUP BY CUBE(
    c.name,
    d.name,
    EXTRACT(YEAR FROM a.posted_at),
    EXTRACT(MONTH FROM a.posted_at)
)
ORDER BY c.name, d.name, year, month;

SELECT
    c.name AS city,
    d.name AS district,
    EXTRACT(YEAR FROM a.posted_at) AS year,
    EXTRACT(MONTH FROM a.posted_at) AS month,
    COUNT(*) AS total_ads,
    ROUND(AVG(CASE WHEN type = 'sale' then a.price else null end), 2) AS avg_sale_price,
    ROUND(AVG(CASE WHEN type = 'rent' then a.price else null end), 2) AS avg_rent_price,
    ROUND(AVG(CASE WHEN type = 'sale' then a.price else null end), 2) AS avg_sale_price_m2,
    ROUND(AVG(CASE WHEN type = 'rent' then a.price else null end), 2) AS avg_rent_price_m2
FROM advertisements a
JOIN apartments ap ON a.apartment_id = ap.id
JOIN districts d ON ap.district_id = d.id
JOIN cities c ON d.city_id = c.id
GROUP BY c.name, d.name, EXTRACT(YEAR FROM a.posted_at), EXTRACT(MONTH FROM a.posted_at);

SELECT
    c.name AS city,
    d.name AS district,
    EXTRACT(YEAR FROM a.posted_at) AS year,
    EXTRACT(MONTH FROM a.posted_at) AS month,
    COUNT(*) AS total_ads,
    ROUND(AVG(CASE WHEN type = 'sale' then a.price else null end), 2) AS avg_sale_price,
    ROUND(AVG(CASE WHEN type = 'rent' then a.price else null end), 2) AS avg_rent_price,
    ROUND(AVG(CASE WHEN type = 'sale' then a.price else null end), 2) AS avg_sale_price_m2,
    ROUND(AVG(CASE WHEN type = 'rent' then a.price else null end), 2) AS avg_rent_price_m2
FROM advertisements a
JOIN apartments ap ON a.apartment_id = ap.id
JOIN districts d ON ap.district_id = d.id
JOIN cities c ON d.city_id = c.id
GROUP BY c.name, d.name, EXTRACT(YEAR FROM a.posted_at), EXTRACT(MONTH FROM a.posted_at);


SELECT
    c.name AS city,
    d.name AS district,
    EXTRACT(YEAR FROM a.posted_at) AS year,
    EXTRACT(MONTH FROM a.posted_at) AS month,
    EXTRACT(DAY FROM a.posted_at) AS day,
    COUNT(*) OVER (PARTITION BY c.name, d.name, EXTRACT(YEAR FROM a.posted_at), EXTRACT(MONTH FROM a.posted_at)) AS total_ads,
    ROUND(AVG(a.price) OVER (PARTITION BY c.name, d.name, EXTRACT(YEAR FROM a.posted_at), EXTRACT(MONTH FROM a.posted_at)), 2) AS avg_price
FROM advertisements a
JOIN apartments ap ON a.apartment_id = ap.id
JOIN districts d ON ap.district_id = d.id
JOIN cities c ON d.city_id = c.id
WHERE c.name = 'Aizkraukle un rajons';


select EXTRACT(YEAR FROM posted_at) as year, count(*), 
    ROUND(AVG(CASE WHEN type = 'sale' then a.price else null end), 2) AS avg_sale_price,
    ROUND(AVG(CASE WHEN type = 'rent' then a.price else null end), 2) AS avg_rent_price,
    ROUND(AVG(CASE WHEN type = 'sale' then a.price else null end), 2) AS avg_sale_price_m2,
    ROUND(AVG(CASE WHEN type = 'rent' then a.price else null end), 2) AS avg_rent_price_m2 
from advertisements  a
group by EXTRACT(YEAR FROM posted_at);

sel