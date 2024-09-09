package com.flightsearch.backend.services;

import com.flightsearch.backend.dtos.AirportCityDTO;
import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.dtos.FlightResponseDTO;

import java.util.List;

public interface FlightService {
    String testConnection();
    List<AirportCityDTO> getAirportsAndCities(String keyword);
    List<FlightResponseDTO> searchFlights(FlightRequestDTO request);
}
