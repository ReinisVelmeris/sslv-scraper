package com.scraper.sslv_scraper.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.scraper.sslv_scraper.model.Advertisement;
import com.scraper.sslv_scraper.model.Apartment;
import com.scraper.sslv_scraper.model.City;
import com.scraper.sslv_scraper.model.District;
import com.scraper.sslv_scraper.repository.AdvertisementRepository;
import com.scraper.sslv_scraper.repository.ApartmentRepository;
import com.scraper.sslv_scraper.repository.CityRepository;
import com.scraper.sslv_scraper.repository.DistrictRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ScraperService {
    private final AdvertisementRepository advertisementRepository;
    private final ApartmentRepository apartmentRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;

    static String baseUrl = "https://www.ss.lv";

    public List<Advertisement> exctractData(String listingUrl) {
        List<Advertisement> advertisements = new ArrayList<>();
        int pageNumber = 1;
        boolean firstPageVisited = false;
        int currentAdvertIndex = 0;

        do {
            try {
                String urlAndPage = listingUrl + "/page" + pageNumber + ".html";
                Document doc = Jsoup.connect(urlAndPage).get();
                

                Elements elements = doc.select(".msga2-o");
            
                Elements descriptions = doc.select(".d1 a");

                

                String link = doc.selectFirst("link[rel=alternate][media]").attr("href");
                if (link == null) {
                    System.out.println("No link[rel=alternate][media] found on page " + urlAndPage);
                    break;
                }

                if ((link.endsWith("/") || link.endsWith("/page1.html")) && firstPageVisited == true) {
                    break;
                }

                // elements = 210 : 7 = 30 ieraksti
                for (int i = 0; i <= elements.size() - 7; i += 7) {
                    District district = districtRepository.findByUrlPath(listingUrl.replace(baseUrl, ""));

                    String street = elements.get(i).text();
                    String rooms = elements.get(i + 1).text();
                    double area = parsePrice(elements.get(i + 2).text());
                    String floor = elements.get(i + 3).text();
                    String series = elements.get(i + 4).text();
                    double pricePerSquareMeter = parsePrice(elements.get(i + 5).text());
                    double price = parsePrice(elements.get(i + 6).text());
                    String url = descriptions.get(currentAdvertIndex).attr("href");
                    String description = descriptions.get(currentAdvertIndex).text();

                    Apartment apartment = apartmentRepository
                            .findByDistrictAndStreetAndRoomNoAndArea(district, street, rooms, area)
                            .orElseGet(() -> {
                                Apartment newApartment = new Apartment();
                                newApartment.setStreet(street);
                                newApartment.setRooms(rooms);
                                newApartment.setArea(area);
                                newApartment.setFloor(floor);
                                newApartment.setSeries(series);
                                newApartment.setDistrict(district);
                                return apartmentRepository.save(newApartment);
                            });

                    Advertisement advertisement = advertisementRepository
                            .findByUrl(url)
                            .orElseGet(() -> {
                                Advertisement newAd = new Advertisement();
                                newAd.setApartment(apartment);
                                newAd.setDescription(description);
                                newAd.setUrl(url);
                                newAd.setPostedAt(LocalDateTime.now());
                                newAd.setPrice(price);
                                newAd.setPricePerSquareMeter(pricePerSquareMeter);
                                return advertisementRepository.save(newAd);
                            });
                    advertisements.add(advertisement);
                    currentAdvertIndex++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            pageNumber++;
            currentAdvertIndex = 0;
            firstPageVisited = true;
        } while (true);

        return advertisements;
    }

    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public List<String> getAllRealEstateLinks() {
        String realEstateUrl = baseUrl + "lv/real-estate/flats/";
        List<String> linksForScraping = new ArrayList<>();

        // First, get all the cities and save them to the database
        try {
            Document cityDoc = Jsoup.connect(realEstateUrl).get();
            Elements cityLinks = cityDoc.select("a.a_category");
            for (Element link : cityLinks) {
                String cityUrl = link.attr("href");
                String citySlug = cityUrl.split("/")[4];
                String cityName = link.text().trim();
                if (cityRepository.existsBySlug(citySlug)) {
                    continue;
                }
                City city = new City();
                city.setName(cityName);
                city.setSlug(citySlug);
                city.setUrlPath(cityUrl);
                cityRepository.save(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then, get all the districts and save them to the database
        try {
            Queue<String> queue = new LinkedList<>();
            queue.add(realEstateUrl);

            while (!queue.isEmpty()) {
                String currentUrl = queue.poll();
                Document doc = Jsoup.connect(currentUrl).get();
                Elements subCategoryLinks = doc.select("a.a_category");

                if (subCategoryLinks.isEmpty()) {
                    linksForScraping.add(currentUrl);
                } else {
                    for (Element link : subCategoryLinks) {
                        String href = link.attr("href");
                        String subCategoryUrl = baseUrl + href;
                        queue.add(subCategoryUrl);

                        String[] parts = href.split("/");
                        if (parts.length >= 6) {
                            String citySlug = parts[4];
                            String districtSlug = parts[5];
                            String districtName = link.text().trim();
                            String cityName = citySlug;

                            City city = cityRepository.findBySlug(citySlug)
                                    .orElseGet(() -> {
                                        City newCity = new City();
                                        newCity.setName(cityName);
                                        newCity.setSlug(citySlug);
                                        return cityRepository.save(newCity);
                                    });
                            Optional<District> existingDistrictOpt = districtRepository.findBySlugAndCity(districtSlug,
                                    city);
                            District district = existingDistrictOpt.orElseGet(District::new);
                            district.setName(districtName);
                            district.setSlug(districtSlug);
                            district.setCity(city);
                            district.setUrlPath((currentUrl + districtSlug).replace("https://www.ss.lv/", ""));

                            districtRepository.save(district);
                        }
                    }
                }
            }
        } catch (HttpStatusException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return linksForScraping;
    }

    public List<Advertisement> scrapeAllAdvertisements() {
        List<String> links = districtRepository.findAll().stream()
                .map(district -> baseUrl + district.getUrlPath())
                .toList();
        List<Advertisement> allAdvertisements = new ArrayList<>();

        advertisementRepository.deleteAll();
        apartmentRepository.deleteAll();
        for (String link : links) {
            List<Advertisement> advertisements = exctractData(link);
            allAdvertisements.addAll(advertisements);
        }
        return allAdvertisements;
    }

}