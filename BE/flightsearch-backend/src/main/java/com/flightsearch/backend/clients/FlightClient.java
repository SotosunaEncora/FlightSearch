package com.flightsearch.backend.clients;

import com.flightsearch.backend.dtos.AirportCityDTO;
import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.dtos.FlightResponseDTO;

import java.util.List;

public interface FlightClient {
    String testConnection();
    List<AirportCityDTO> getAirportsAndCities(String keyword);
    List<FlightResponseDTO> searchFlights(FlightRequestDTO request);
}
