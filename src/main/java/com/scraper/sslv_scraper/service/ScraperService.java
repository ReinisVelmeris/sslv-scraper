package com.scraper.sslv_scraper.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
import com.scraper.sslv_scraper.config.EntityClassRegistry;
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

    private final EntityClassRegistry entityClassRegistry;
    private final AdvertisementRepository advertisementRepository;
    private final ApartmentRepository apartmentRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;

    static String baseUrl = "https://www.ss.lv/";

    public List<Advertisement> exctractData(String listingUrl) {
        int pageNumber = 1;
        boolean firstPageVisited = false;
        int currentAdvertIndex = 0;
        Elements elements = null;
        Elements descriptions = null;
        String urlAndPage = null;
        Document doc = null;
        String link = "";
        List<Advertisement> advertisements = new ArrayList<Advertisement>();

        // String street = "";
        // String rooms = "";
        // double area = 0.0;
        // String floor = "";
        // String series = "";
        // double pricePerSquareMeter = 0.0;
        // double price = 0.0;
        // String url = "";
        // String description = "";
        // String type = "";

        do {
            try {
                urlAndPage = listingUrl + "/page" + pageNumber + ".html";
                doc = Jsoup.connect(urlAndPage)
                        .userAgent(
                                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .header("Connection", "keep-alive")
                        .timeout(10000)
                        .get();

                elements = doc.select(".msga2-o");

                descriptions = doc.select(".d1 a");

                link = "";

                try {
                    link = doc.selectFirst("link[rel=alternate][media]").attr("href");
                } catch (NullPointerException nullPointerException) {
                    System.out.println("URL: " + urlAndPage);
                    break;
                }

                if ((link.endsWith("/") || link.endsWith("/page1.html")) && firstPageVisited == true) {
                    break;
                }

                // elements = 210 : 7 = 30 ieraksti
                for (int i = 0; i <= elements.size() - 7; i += 7) {
                    District district = districtRepository.findByUrlPath(listingUrl.replace(baseUrl, ""));
                    String priceText = elements.get(i + 6).text();
                    String[] priceComponents = priceText.split(" ");

                    String street = elements.get(i).text();
                    String rooms = elements.get(i + 1).text();
                    double area = parsePrice(elements.get(i + 2).text());
                    String floor = elements.get(i + 3).text();
                    String series = elements.get(i + 4).text();
                    double pricePerSquareMeter = parsePrice(elements.get(i + 5).text());
                    double price = parsePrice(priceComponents[0]);
                    String url = descriptions.get(currentAdvertIndex).attr("href");
                    String description = descriptions.get(currentAdvertIndex).text();
                    String type = (priceText == "pērku") ? "buy"
                            : (priceComponents[1].matches(".*€/mēn\\.|.*€/dienā.*")) ? "rent" : "sale";

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

                    Advertisement ad = advertisementRepository.findByUrl(url).orElse(new Advertisement());

                    ad.setApartment(apartment);
                    ad.setDescription(description);
                    ad.setUrl(url);
                    ad.setPostedAt(LocalDateTime.now());
                    ad.setPrice(price);
                    ad.setPricePerSquareMeter(pricePerSquareMeter);
                    ad.setType(type);

                    //Enter the advertisement and extract publish date and number of views
                    Document adDoc = Jsoup.connect(baseUrl + url).get();
                    Element dateElement = adDoc.select("td.msg_footer:contains(Datums:)").last();
                    Element viewElement = adDoc.selectFirst("#show_cnt_stat");
                    String text = dateElement.text();
                    String dateStr = "";

                    if (dateElement != null) {
                        dateStr = text.replace("Datums: ", "");
                    }
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                ad.setPostedAt(dateTime);
                ad.setViews(Integer.parseInt(viewElement.text()));

                    advertisementRepository.save(ad);
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
        districtRepository.deleteAll();
        try {
            Queue<String> queue = new LinkedList<>();
            queue.add(realEstateUrl);

            while (!queue.isEmpty()) {
                String currentUrl = queue.poll();
                Document doc = Jsoup.connect(currentUrl).get();
                Elements subCategoryLinks = doc.select("a.a_category");

                if (subCategoryLinks.isEmpty()) {
                    linksForScraping.add(currentUrl);
                    String[] parts = currentUrl.split("/");
                    String citySlug = parts[7];
                    String districtSlug = parts[parts.length - 1];
                    String districtName = "";
                    try {
                        districtName = doc.selectFirst("select[class=filter_sel] option[selected]").text();
                    } catch (NullPointerException npe) {
                        System.out.println(currentUrl);
                    }
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
                    district.setUrlPath(currentUrl.replace("https://www.ss.lv/", ""));
                    districtRepository.save(district);
                } else {
                    for (Element link : subCategoryLinks) {
                        String href = link.attr("href");
                        String subCategoryUrl = baseUrl + href;
                        queue.add(subCategoryUrl);
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

        List<Advertisement> allAdvertisements = new ArrayList<>();

        List<String> links = districtRepository.findAll().stream()
                .map(district -> baseUrl + district.getUrlPath())
                .toList();

        List<Advertisement> advertisements = new ArrayList<>();
        for (String link : links) {
            System.out.println(link);
            advertisements = exctractData(link);
            // allAdvertisements.addAll(advertisements);
        }
        return allAdvertisements;
    }

}