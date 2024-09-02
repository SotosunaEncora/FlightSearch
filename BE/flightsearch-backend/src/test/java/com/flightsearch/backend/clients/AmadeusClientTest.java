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
        // Mock the API response
        String mockJsonResponse = "{\n" +
            "  \"data\": [{\n" +
            "    \"price\": {\n" +
            "      \"base\": \"100.00\",\n" +
            "      \"total\": \"120.00\",\n" +
            "      \"fees\": [{\"amount\": \"20.00\"}],\n" +
            "      \"currency\": \"USD\"\n" +
            "    },\n" +
            "    \"itineraries\": [{\n" +
            "      \"duration\": \"PT5H\",\n" +
            "      \"segments\": [{\n" +
            "        \"departure\": {\"iataCode\": \"JFK\", \"at\": \"2023-07-01T10:00:00\"},\n" +
            "        \"arrival\": {\"iataCode\": \"LAX\", \"at\": \"2023-07-01T15:00:00\"},\n" +
            "        \"carrierCode\": \"AA\",\n" +
            "        \"number\": \"123\",\n" +
            "        \"aircraft\": {\"code\": \"738\"},\n" +
            "        \"duration\": \"PT5H\"\n" +
            "      }]\n" +
            "    }],\n" +
            "    \"travelerPricings\": [{\n" +
            "      \"fareDetailsBySegment\": [{\n" +
            "        \"cabin\": \"ECONOMY\",\n" +
            "        \"class\": \"Y\",\n" +
            "        \"fareBasis\": \"Y1\",\n" +
            "        \"includedCheckedBags\": {\"weight\": 23, \"weightUnit\": \"KG\"}\n" +
            "      }]\n" +
            "    }]\n" +
            "  }]\n" +
            "}";

        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockJsonResponse, HttpStatus.OK);
        when(restTemplate.exchange(
            contains("/v2/shopping/flight-offers"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);

        FlightRequestDTO request = new FlightRequestDTO();
        request.setDepartureAirport("JFK");
        request.setArrivalAirport("LAX");
        request.setDepartureDate("2023-07-01");
        request.setAdults(1);
        request.setCurrency("USD");
        request.setNonStop(true);

        List<FlightResponseDTO> result = amadeusClient.searchFlights(request);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("JFK", result.get(0).getOrigin());
        assertEquals("LAX", result.get(0).getDestination());
        assertEquals("2023-07-01T10:00:00", result.get(0).getAwayDepartureTime());
        assertEquals("2023-07-01T15:00:00", result.get(0).getAwayArrivalTime());
        assertEquals(120.0, result.get(0).getPriceBreakdown().getTotal());
    }
}
