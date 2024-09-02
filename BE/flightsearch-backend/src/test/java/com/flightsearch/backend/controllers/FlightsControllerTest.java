package com.flightsearch.backend.controllers;

import com.flightsearch.backend.clients.AmadeusClient;
import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.dtos.FlightResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FlightsControllerTest {

    @Mock
    private AmadeusClient amadeusClient;

    @InjectMocks
    private FlightsController flightsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchFlights() {
        // Arrange
        FlightRequestDTO request = new FlightRequestDTO();
        // Set up the request object as needed

        FlightResponseDTO mockResponse = new FlightResponseDTO();
        // Set up the mock response as needed

        when(amadeusClient.searchFlights(any(FlightRequestDTO.class)))
            .thenReturn(Arrays.asList(mockResponse));

        // Act
        ResponseEntity<List<FlightResponseDTO>> result = flightsController.searchFlights(request);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(mockResponse, result.getBody().get(0));

        verify(amadeusClient).searchFlights(request);
    }

    // Add more tests for error handling, edge cases, etc.
}
