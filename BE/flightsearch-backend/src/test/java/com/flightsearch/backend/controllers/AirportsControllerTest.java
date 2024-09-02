package com.flightsearch.backend.controllers;

import com.flightsearch.backend.clients.AmadeusClient;
import com.flightsearch.backend.dtos.AirportCityDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AirportsControllerTest {

    @Mock
    private AmadeusClient amadeusClient;

    @InjectMocks
    private AirportsController airportsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAirports() {
        // Arrange
        String keyword = "New York";
        List<AirportCityDTO> mockResponse = Arrays.asList(
            new AirportCityDTO("JFK", "John F. Kennedy International Airport", "New York"),
            new AirportCityDTO("LGA", "LaGuardia Airport", "New York")
        );
        when(amadeusClient.getAirportsAndCities(keyword)).thenReturn(mockResponse);

        // Act
        ResponseEntity<List<AirportCityDTO>> result = airportsController.fetchAirports(keyword);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        assertEquals("JFK", result.getBody().get(0).getCode());
        assertEquals("LGA", result.getBody().get(1).getCode());

        verify(amadeusClient).getAirportsAndCities(keyword);
    }

    @Test
    void fetchAirportsNoResults() {
        // Arrange
        String keyword = "NonexistentCity";
        when(amadeusClient.getAirportsAndCities(keyword)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<AirportCityDTO>> result = airportsController.fetchAirports(keyword);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());

        verify(amadeusClient).getAirportsAndCities(keyword);
    }
}
