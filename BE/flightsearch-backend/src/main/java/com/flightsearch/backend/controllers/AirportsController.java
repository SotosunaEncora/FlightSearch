package com.flightsearch.backend.controllers;

import com.flightsearch.backend.clients.AmadeusClient;
import com.flightsearch.backend.dtos.AirportCityDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportsController {

    private final AmadeusClient amadeusClient;

    public AirportsController(AmadeusClient amadeusClient) {
        this.amadeusClient = amadeusClient;
    }

    @GetMapping
    public ResponseEntity<List<AirportCityDTO>> fetchAirports(@RequestParam String keyword) {
        try {
            List<AirportCityDTO> airports = amadeusClient.getAirportsAndCities(keyword);
            return new ResponseEntity<>(airports, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
