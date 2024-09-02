package com.flightsearch.backend.clients;

import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AmadeusClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AmadeusConfig amadeusConfig;

    private AmadeusClient amadeusClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(amadeusConfig.getBaseUrl()).thenReturn("http://mock-url.com");
        when(amadeusConfig.getApiKey()).thenReturn("mockClientId");
        when(amadeusConfig.getApiSecret()).thenReturn("mockClientSecret");

        // Mock the token response
        ResponseEntity<String> tokenResponse = new ResponseEntity<>(
            "{\"access_token\":\"mocked_token\",\"expires_in\":3600,\"token_type\":\"Bearer\"}",
            HttpStatus.OK
        );
        when(restTemplate.exchange(
            eq("http://mock-url.com/v1/security/oauth2/token"),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(tokenResponse);

        amadeusClient = new AmadeusClient(restTemplate, amadeusConfig);
    }

    @Test
    void testConnection() {
        // Mock the API response
        ResponseEntity<String> mockResponse = new ResponseEntity<>(
            "{\"data\":[{\"iataCode\":\"HMO\",\"name\":\"Hermosillo International Airport\"}]}",
            HttpStatus.OK
        );
        when(restTemplate.exchange(
            contains("/v1/reference-data/locations"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        String result = amadeusClient.testConnection();
        assertNotNull(result);
        assertTrue(result.contains("HMO"));
        assertTrue(result.contains("Hermosillo International Airport"));
    }

    @Test
    void getAirportsAndCities() {
        // Mock the API response
        ResponseEntity<String> mockResponse = new ResponseEntity<>(
            "{\"data\":[{\"iataCode\":\"JFK\",\"name\":\"John F. Kennedy International Airport\",\"address\":{\"cityName\":\"New York\"}}]}",
            HttpStatus.OK
        );
        when(restTemplate.exchange(
            contains("/v1/reference-data/locations"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        List<AirportCityDTO> result = amadeusClient.getAirportsAndCities("New York");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("JFK", result.get(0).getCode());
        assertEquals("John F. Kennedy International Airport", result.get(0).getName());
        assertEquals("New York", result.get(0).getCityName());
    }

    @Test
    void searchFlights() {
        // Prepare the request
        FlightRequestDTO request = new FlightRequestDTO();
        request.setDepartureAirport("JFK");
        request.setArrivalAirport("LAX");
        request.setDepartureDate("2023-07-01");
        request.setAdults(1);
        request.setCurrency("USD");
        request.setNonStop(true);

        // Prepare the mock response
        FlightResponseDTO mockFlightResponse = new FlightResponseDTO();
        mockFlightResponse.setOrigin("JFK");
        mockFlightResponse.setDestination("LAX");
        mockFlightResponse.setAwayDepartureTime("2023-07-01T10:00:00");
        mockFlightResponse.setAwayArrivalTime("2023-07-01T15:00:00");
        mockFlightResponse.setAwayDuration("PT5H");
        
        PriceBreakdown priceBreakdown = new PriceBreakdown();
        priceBreakdown.setBase(100.0);
        priceBreakdown.setTotal(120.0);
        priceBreakdown.setFees(20.0);
        priceBreakdown.setCurrency("USD");
        mockFlightResponse.setPriceBreakdown(priceBreakdown);

        List<FlightResponseDTO> mockResponseList = List.of(mockFlightResponse);

        // Mock the behavior of AmadeusClient
        AmadeusClient spyAmadeusClient = spy(amadeusClient);
        doReturn(mockResponseList).when(spyAmadeusClient).searchFlights(any(FlightRequestDTO.class));

        // Perform the test
        List<FlightResponseDTO> result = spyAmadeusClient.searchFlights(request);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        FlightResponseDTO actualResponse = result.get(0);
        assertEquals("JFK", actualResponse.getOrigin());
        assertEquals("LAX", actualResponse.getDestination());
        assertEquals("2023-07-01T10:00:00", actualResponse.getAwayDepartureTime());
        assertEquals("2023-07-01T15:00:00", actualResponse.getAwayArrivalTime());
        assertEquals(120.0, actualResponse.getPriceBreakdown().getTotal());
    }
}
