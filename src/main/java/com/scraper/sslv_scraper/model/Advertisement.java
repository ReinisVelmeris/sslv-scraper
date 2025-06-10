package com.scraper.sslv_scraper.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)

    private String description;
    private double price;
    private double pricePerSquareMeter;
    private LocalDateTime postedAt;
    private String url;
    private String type;
    @Column(nullable = true)
    private Integer views;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

}