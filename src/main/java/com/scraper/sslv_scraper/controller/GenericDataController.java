package com.scraper.sslv_scraper.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scraper.sslv_scraper.dto.DynamicFilterRequest;
import com.scraper.sslv_scraper.service.GenericDataService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/data")
@AllArgsConstructor
public class GenericDataController {
    private final GenericDataService dataService;

@PostMapping("/{entity}")
public ResponseEntity<?> getRequestedData(
        @PathVariable String entity,
        @Valid @RequestBody DynamicFilterRequest request
) {
    try {
        List<?> result = dataService.filter(entity, request.getFilters());
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (UnsupportedOperationException e) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
    }
}
    
}
