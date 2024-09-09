package com.flightsearch.backend.controllers;

import com.flightsearch.backend.dtos.FlightResponseDTO;
import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightsController {
    private final FlightService flightService;

    @Autowired
    public FlightsController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(@RequestBody FlightRequestDTO request) {
        List<FlightResponseDTO> results = flightService.searchFlights(request);
        return ResponseEntity.ok(results);
    }
}
