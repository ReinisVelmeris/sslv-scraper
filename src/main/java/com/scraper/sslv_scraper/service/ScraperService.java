package com.scraper.sslv_scraper.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.scraper.sslv_scraper.model.Advertisement;
import com.scraper.sslv_scraper.repository.AdvertisementRepository;

@Service
public class ScraperService {
    private final AdvertisementRepository advertisementRepository;
    static String url = "https://www.ss.lv/lv/real-estate/flats/";


    public ScraperService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public List<Advertisement> exctractData(String city){
        String cityUrl = "https://www.ss.lv/lv/real-estate/flats/riga/centre/";
        List<Advertisement> advertisements = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(cityUrl).get();
            Elements elements = doc.select(".msga2-o");
            System.out.println("Elements: " + elements.toString());

            Advertisement ad = new Advertisement();
            for(int i=0; i < elements.size(); i += 7){
                ad.setStreet(elements.get(i).text());
                ad.setRoom(elements.get(i+1).text());
                ad.setArea(Double.valueOf(parsePrice(elements.get(i+2).text())));
                ad.setFloor(elements.get(i+3).text());
                ad.setSeries(elements.get(i+4).text());
                ad.setPricePerSquareMeter(Double.valueOf(parsePrice(elements.get(i+5).text())));
                ad.setPrice(Double.valueOf(parsePrice(elements.get(i+6).text())));

                advertisementRepository.save(ad);
                advertisements.add(ad);
                ad = new Advertisement();   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return advertisements;
    }

    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public List<Advertisement> exctractData(String city, String district) {
        String cityUrl = url + city + "/" + district;
        List<Advertisement> advertisements = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(cityUrl).get();
            Elements elements = doc.select("table tbody tr");
            for (Element element : elements) {
                
                String pricePerSquareMeter = element.select(".msga2-o pp6").text();
                String price = element.select("msga2-o pp6").text();
                String street = element.select("msga2-o pp6").text();
                String area = element.select("msga2-o pp6").text();
                String floor = element.select("msga2-o pp6").text();
                String roomNo = element.select("msga2-o pp6").text();

                Advertisement ad = new Advertisement();
                ad.setTitle(street + " - " + roomNo + " rooms"); // example title
                ad.setDescription("Area: " + area + ", Floor: " + floor);
                ad.setPrice(parsePrice(price));
                ad.setPostedAt(LocalDateTime.now());

                advertisementRepository.save(ad);
                advertisements.add(ad);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return advertisements;
    }
    
}
