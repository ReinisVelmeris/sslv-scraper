select * from cities;
SELECT * from districts;
select count(*) from advertisements;
select * from advertisements;
select * from APARTMENTS;
select count(*) from apartments;

SELECT * FROM advertisements order by url ASC;

SELECT count(*) FROM advertisements ad
JOIN apartments ap on ap.id= ad.apartment_id
JOIN districts d on ap.district_id = d.id
JOIN cities c on c.id = d.city_id
WHERE d.URL_PATH like '%centre%';
