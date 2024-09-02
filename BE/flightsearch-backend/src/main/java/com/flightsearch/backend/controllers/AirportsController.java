package com.flightsearch.backend.controllers;

import com.flightsearch.backend.clients.AmadeusClient;
import com.flightsearch.backend.dtos.AirportCityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/airports")
public class AirportsController {
    private final AmadeusClient amadeusClient;

    @Autowired
    public AirportsController(AmadeusClient amadeusClient) {
        this.amadeusClient = amadeusClient;
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<List<AirportCityDTO>> fetchAirports(@PathVariable String keyword) {
        System.out.println("Controller Called!!!");
        List<AirportCityDTO> results;
        try {
            results = amadeusClient.getAirportsAndCities(keyword);
        } catch (Exception e) {
            System.err.println("Error connecting to Amadeus API, returning mock data: " + e.getMessage());
            results = getMockAirports(keyword);
        }
        return ResponseEntity.ok(results);
    }

    private List<AirportCityDTO> getMockAirports(String keyword) {
        List<AirportCityDTO> mockAirports = new ArrayList<>();
        mockAirports.add(new AirportCityDTO("MOCK1", "Mock Airport 1", "Mock City 1"));
        mockAirports.add(new AirportCityDTO("MOCK2", "Mock Airport 2", "Mock City 2"));
        mockAirports.add(new AirportCityDTO("JFK", "John F. Kennedy International Airport", "New York"));
        mockAirports.add(new AirportCityDTO("LAX", "Los Angeles International Airport", "Los Angeles"));
        mockAirports.add(new AirportCityDTO("ORD", "O'Hare International Airport", "Chicago"));

        return mockAirports.stream()
                .filter(airport -> airport.getCode().toLowerCase().contains(keyword.toLowerCase()) ||
                                   airport.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                   airport.getCityName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
