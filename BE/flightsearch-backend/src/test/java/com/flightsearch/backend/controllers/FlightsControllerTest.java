package com.flightsearch.backend.controllers;

import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.dtos.FlightResponseDTO;
import com.flightsearch.backend.services.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FlightsControllerTest {

    @Mock
    private FlightService flightService;

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
        List<FlightResponseDTO> expectedResponse = new ArrayList<>();
        when(flightService.searchFlights(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<List<FlightResponseDTO>> response = flightsController.searchFlights(request);

        // Assert
        assertEquals(expectedResponse, response.getBody());
        verify(flightService, times(1)).searchFlights(request);
    }
}
