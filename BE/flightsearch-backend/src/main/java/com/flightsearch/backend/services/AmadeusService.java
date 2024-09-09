package com.flightsearch.backend.services;

import com.flightsearch.backend.clients.FlightClient;
import com.flightsearch.backend.dtos.AirportCityDTO;
import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.dtos.FlightResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmadeusService implements FlightService {

    private final FlightClient flightClient;

    @Autowired
    public AmadeusService(FlightClient flightClient) {
        this.flightClient = flightClient;
    }

    @Override
    public String testConnection() {
        return flightClient.testConnection();
    }

    @Override
    public List<AirportCityDTO> getAirportsAndCities(String keyword) {
        return flightClient.getAirportsAndCities(keyword);
    }

    @Override
    public List<FlightResponseDTO> searchFlights(FlightRequestDTO request) {
        return flightClient.searchFlights(request);
    }
}
