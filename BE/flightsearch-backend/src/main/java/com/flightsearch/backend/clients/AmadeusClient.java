package com.flightsearch.backend.clients;

import com.flightsearch.backend.dtos.AirportCityDTO;
import com.flightsearch.backend.config.AmadeusConfig;
import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.dtos.FlightResponseDTO;
import com.flightsearch.backend.dtos.Segment;
import com.flightsearch.backend.dtos.FareDetails;
import com.flightsearch.backend.dtos.PriceBreakdown;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Component
public class AmadeusClient {

    private final RestTemplate restTemplate;
    private final AmadeusConfig amadeusConfig;

    private String accessToken;
    private long tokenExpiryTime;

    @Autowired
    public AmadeusClient(RestTemplate restTemplate, AmadeusConfig amadeusConfig) {
        this.restTemplate = restTemplate;
        this.amadeusConfig = amadeusConfig;
        System.out.println("AmadeusClient initialized.");
        obtainAccessToken();
    }

    // Method to obtain the OAuth2 token
    private void obtainAccessToken() {
        System.out.println("Attempting to obtain access token from Amadeus API");
        String url = amadeusConfig.getBaseUrl() + "/v1/security/oauth2/token";
        System.out.println("url: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        System.out.println("headers: " + headers.toString());

        String body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s",
                                    amadeusConfig.getApiKey(), amadeusConfig.getApiSecret());

        System.out.println(body);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        System.out.println(jsonObject);

        accessToken = jsonObject.getString("access_token");

        tokenExpiryTime = System.currentTimeMillis() + jsonObject.getInt("expires_in") * 1000L;

        System.out.println("Access token obtained: " + accessToken);
        System.out.println(tokenExpiryTime);
    }

    // Ensure the token is valid (and refresh it if necessary)
    private void ensureValidToken() {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpiryTime) {
            obtainAccessToken();
        }
    }

    public String testConnection() {
        System.out.println("Searching for locations");
        // Ensure the token is valid before making the request
        ensureValidToken();

        // Prepare the request for the flight search
        String url = amadeusConfig.getBaseUrl() + "/v1/reference-data/locations?subType=AIRPORT&keyword=HMO";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Add the access token as a Bearer token
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println("API Response: " + response.getBody());
        return response.getBody(); // Return the response (in reality, you'd parse this JSON response)
    }

    public List<AirportCityDTO> getAirportsAndCities(String keyword) {
        try {
            String url = amadeusConfig.getBaseUrl() + "/v1/reference-data/locations?subType=AIRPORT&keyword=" + keyword;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Parse the response using org.json
            JSONObject root = new JSONObject(response.getBody());
            JSONArray data = root.getJSONArray("data");

            List<AirportCityDTO> airportsAndCities = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject node = data.getJSONObject(i);
                String code = node.getString("iataCode");
                String name = node.getString("name");
                String cityName = "N/A";
                if (node.has("address") && node.getJSONObject("address").has("cityName")) {
                    cityName = node.getJSONObject("address").getString("cityName");
                }
                AirportCityDTO dto = new AirportCityDTO(code, name, cityName);
                airportsAndCities.add(dto);
            }

            return airportsAndCities;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch airports from Amadeus API", e);
        }
    }

    public List<FlightResponseDTO> searchFlights(FlightRequestDTO request) {
        try {
            String url = String.format("%s/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=%d&currencyCode=%s&nonStop=%b",
                    amadeusConfig.getBaseUrl(),
                    request.getDepartureAirport(),
                    request.getArrivalAirport(),
                    request.getDepartureDate(),
                    request.getAdults(),
                    request.getCurrency(),
                    request.isNonStop()
            );
            if (request.getReturnDate() != null) {
                url += "&returnDate=" + request.getReturnDate();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Parse the response using org.json
            JSONObject root = new JSONObject(response.getBody());
            JSONArray data = root.getJSONArray("data");

            List<FlightResponseDTO> flights = new ArrayList<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject flightOffer = data.getJSONObject(i);
                FlightResponseDTO flightResponse = new FlightResponseDTO();

                // Parse price breakdown
                JSONObject price = flightOffer.getJSONObject("price");
                PriceBreakdown priceBreakdown = new PriceBreakdown(
                        price.getDouble("base"),
                        price.getDouble("total"),
                        price.getJSONArray("fees").getJSONObject(0).getDouble("amount"),
                        price.getDouble("total") / request.getAdults(),
                        price.getString("currency")
                );
                flightResponse.setPriceBreakdown(priceBreakdown);

                // Parse itineraries and segments
                JSONArray itineraries = flightOffer.getJSONArray("itineraries");
                List<Segment> departureSegments = new ArrayList<>();
                List<Segment> returnSegments = new ArrayList<>();

                for (int j = 0; j < itineraries.length(); j++) {
                    JSONObject itinerary = itineraries.getJSONObject(j);
                    JSONArray segments = itinerary.getJSONArray("segments");

                    for (int k = 0; k < segments.length(); k++) {
                        JSONObject segment = segments.getJSONObject(k);
                        Segment seg = new Segment();
                        seg.setOrigin(segment.getJSONObject("departure").getString("iataCode"));
                        seg.setDestination(segment.getJSONObject("arrival").getString("iataCode"));
                        seg.setDepartureTime(segment.getJSONObject("departure").getString("at"));
                        seg.setArrivalTime(segment.getJSONObject("arrival").getString("at"));
                        seg.setAirline(segment.getString("carrierCode"));
                        seg.setFlightNumber(segment.getString("number"));
                        seg.setAircraft(segment.getJSONObject("aircraft").getString("code"));
                        seg.setDuration(segment.getString("duration"));

                        if (j == 0) {
                            flightResponse.setAwayDuration(itinerary.getString("duration"));
                            departureSegments.add(seg);
                        } else {
                            flightResponse.setReturnDuration(itinerary.getString("duration"));
                            returnSegments.add(seg);
                        }
                    }
                }

                // Parse traveler pricings and fare details
                JSONArray travelerPricings = flightOffer.getJSONArray("travelerPricings");
                for (int l = 0; l < travelerPricings.length(); l++) {
                    JSONObject travelerPricing = travelerPricings.getJSONObject(l);
                    JSONArray fareDetailsBySegment = travelerPricing.getJSONArray("fareDetailsBySegment");

                    // Associate fare details with segments in order
                    for (int m = 0; m < fareDetailsBySegment.length(); m++) {
                        JSONObject fareDetail = fareDetailsBySegment.getJSONObject(m);

                        FareDetails fareDetails = new FareDetails();
                        fareDetails.setFareCabin(fareDetail.getString("cabin"));
                        fareDetails.setFareClass(fareDetail.getString("class"));
                        fareDetails.setFareBasis(fareDetail.getString("fareBasis"));
                        List<String> checkedBags = new ArrayList<>();
                        checkedBags.add(fareDetail.getJSONObject("includedCheckedBags").getString("weight") + " " + fareDetail.getJSONObject("includedCheckedBags").getString("weightUnit"));
                        fareDetails.setCheckedBags(checkedBags);

                        // Associate fare details with the corresponding segment
                        if (m < departureSegments.size()) {
                            departureSegments.get(m).setDetails(fareDetails);
                        } else {
                            returnSegments.get(m - departureSegments.size()).setDetails(fareDetails);
                        }
                    }
                }

                flightResponse.setOrigin(departureSegments.get(0).getOrigin());
                flightResponse.setDestination(departureSegments.get(departureSegments.size() - 1).getDestination());
                flightResponse.setAwayDepartureTime(departureSegments.get(0).getDepartureTime());
                flightResponse.setAwayArrivalTime(departureSegments.get(departureSegments.size() - 1).getArrivalTime());
                flightResponse.setReturnDepartureTime(returnSegments.get(0).getDepartureTime());
                flightResponse.setReturnArrivalTime(returnSegments.get(returnSegments.size() - 1).getArrivalTime());
                flightResponse.setDepartureSegments(departureSegments);
                flightResponse.setReturnSegments(returnSegments);

                flights.add(flightResponse);
            }

            return flights;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to fetch flights from Amadeus API", e);
        }
    }
}