package com.flightsearch.backend.controllers;

import com.flightsearch.backend.clients.AmadeusClient;
import com.flightsearch.backend.dtos.FlightResponseDTO;
import com.flightsearch.backend.dtos.FareDetails;
import com.flightsearch.backend.dtos.Segment;
import com.flightsearch.backend.dtos.FlightRequestDTO;
import com.flightsearch.backend.dtos.PriceBreakdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightsController {
    private final AmadeusClient amadeusClient;

    @Autowired
    public FlightsController(AmadeusClient amadeusClient) {
        this.amadeusClient = amadeusClient;
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(@RequestBody FlightRequestDTO request) {
        System.out.println("Flight Controller Called!!!");
        List<FlightResponseDTO> results;
        try {
            results = amadeusClient.searchFlights(request);
        } catch (Exception e) {
            System.err.println("Error connecting to Amadeus API, returning mock data: " + e.getMessage());
            results = getMockFlights();
        }
        // Log the response
        System.out.println("Results: " + results);
        results.forEach(flight -> System.out.println(flight));
        return ResponseEntity.ok(results);
    }

    private List<FlightResponseDTO> getMockFlights() {
        List<FlightResponseDTO> mockFlights = new ArrayList<>();

        // Mock Flight 1
        FlightResponseDTO flight1 = new FlightResponseDTO();
        flight1.setOrigin("SYD");
        flight1.setDestination("BKK");
        flight1.setAwayDepartureTime("2021-11-01T11:35:00");
        flight1.setAwayArrivalTime("2021-11-01T21:50:00");
        flight1.setAirline("PR");
        flight1.setAwayDuration("PT14H15M");

        Segment segment1 = new Segment();
        segment1.setOrigin("SYD");
        segment1.setDestination("MNL");
        segment1.setDepartureTime("2021-11-01T11:35:00");
        segment1.setArrivalTime("2021-11-01T16:50:00");
        segment1.setAirline("PR");
        segment1.setFlightNumber("212");
        segment1.setAircraft("333");
        segment1.setDuration("PT8H15M");

        FareDetails fareDetails1 = new FareDetails();
        fareDetails1.setFareCabin("ECONOMY");
        fareDetails1.setFareClass("E");
        fareDetails1.setFareBasis("EOBAU");
        fareDetails1.setCheckedBags(List.of("25 KG"));
        segment1.setDetails(fareDetails1);

        Segment segment2 = new Segment();
        segment2.setOrigin("MNL");
        segment2.setDestination("BKK");
        segment2.setDepartureTime("2021-11-01T19:20:00");
        segment2.setArrivalTime("2021-11-01T21:50:00");
        segment2.setAirline("PR");
        segment2.setFlightNumber("732");
        segment2.setAircraft("320");
        segment2.setDuration("PT3H30M");

        FareDetails fareDetails2 = new FareDetails();
        fareDetails2.setFareCabin("ECONOMY");
        fareDetails2.setFareClass("E");
        fareDetails2.setFareBasis("EOBAU");
        fareDetails2.setCheckedBags(List.of("25 KG"));
        segment2.setDetails(fareDetails2);

        flight1.setDepartureSegments(List.of(segment1, segment2));

        PriceBreakdown priceBreakdown1 = new PriceBreakdown(255.00, 355.34, 0.00, 355.34, "EUR");
        flight1.setPriceBreakdown(priceBreakdown1);

        mockFlights.add(flight1);

        // Mock Flight 2
        FlightResponseDTO flight2 = new FlightResponseDTO();
        flight2.setOrigin("SYD");
        flight2.setDestination("BKK");
        flight2.setAwayDepartureTime("2021-11-01T11:35:00");
        flight2.setAwayArrivalTime("2021-11-02T00:10:00");
        flight2.setAirline("PR");
        flight2.setAwayDuration("PT14H15M");


        Segment segment3 = new Segment();
        segment3.setOrigin("SYD");
        segment3.setDestination("MNL");
        segment3.setDepartureTime("2021-11-01T11:35:00");
        segment3.setArrivalTime("2021-11-01T16:50:00");
        segment3.setAirline("PR");
        segment3.setFlightNumber("212");
        segment3.setAircraft("333");
        segment3.setDuration("PT8H15M");

        FareDetails fareDetails3 = new FareDetails();
        fareDetails3.setFareCabin("ECONOMY");
        fareDetails3.setFareClass("E");
        fareDetails3.setFareBasis("EOBAU");
        fareDetails3.setCheckedBags(List.of("25 KG"));
        segment3.setDetails(fareDetails3);

        Segment segment4 = new Segment();
        segment4.setOrigin("MNL");
        segment4.setDestination("BKK");
        segment4.setDepartureTime("2021-11-01T21:40:00");
        segment4.setArrivalTime("2021-11-02T00:10:00");
        segment4.setAirline("PR");
        segment4.setFlightNumber("740");
        segment4.setAircraft("321");
        segment4.setDuration("PT3H30M");

        FareDetails fareDetails4 = new FareDetails();
        fareDetails4.setFareCabin("ECONOMY");
        fareDetails4.setFareClass("E");
        fareDetails4.setFareBasis("EOBAU");
        fareDetails4.setCheckedBags(List.of("25 KG"));
        segment4.setDetails(fareDetails4);

        flight2.setDepartureSegments(List.of(segment3, segment4));

        PriceBreakdown priceBreakdown2 = new PriceBreakdown(255.00, 355.34, 0.00, 355.34, "EUR");
        flight2.setPriceBreakdown(priceBreakdown2);

        mockFlights.add(flight2);

        // Mock Flight 3
        FlightResponseDTO flight3 = new FlightResponseDTO();
        flight3.setOrigin("SYD");
        flight3.setDestination("BKK");
        flight3.setAwayDepartureTime("2021-11-02T11:35:00");
        flight3.setAwayArrivalTime("2021-11-02T21:50:00");
        flight3.setAirline("PR");
        flight3.setAwayDuration("PT14H15M");

        Segment segment5 = new Segment();
        segment5.setOrigin("SYD");
        segment5.setDestination("MNL");
        segment5.setDepartureTime("2021-11-02T11:35:00");
        segment5.setArrivalTime("2021-11-02T16:50:00");
        segment5.setAirline("PR");
        segment5.setFlightNumber("212");
        segment5.setAircraft("333");
        segment5.setDuration("PT8H15M");

        FareDetails fareDetails5 = new FareDetails();
        fareDetails5.setFareCabin("ECONOMY");
        fareDetails5.setFareClass("E");
        fareDetails5.setFareBasis("EOBAU");
        fareDetails5.setCheckedBags(List.of("25 KG"));
        segment5.setDetails(fareDetails5);

        Segment segment6 = new Segment();
        segment6.setOrigin("MNL");
        segment6.setDestination("BKK");
        segment6.setDepartureTime("2021-11-02T19:20:00");
        segment6.setArrivalTime("2021-11-02T21:50:00");
        segment6.setAirline("PR");
        segment6.setFlightNumber("732");
        segment6.setAircraft("320");
        segment6.setDuration("PT3H30M");

        FareDetails fareDetails6 = new FareDetails();
        fareDetails6.setFareCabin("ECONOMY");
        fareDetails6.setFareClass("E");
        fareDetails6.setFareBasis("EOBAU");
        fareDetails6.setCheckedBags(List.of("25 KG"));
        segment6.setDetails(fareDetails6);

        flight3.setDepartureSegments(List.of(segment5, segment6));

        PriceBreakdown priceBreakdown3 = new PriceBreakdown(255.00, 355.34, 0.00, 355.34, "EUR");
        flight3.setPriceBreakdown(priceBreakdown3);

        mockFlights.add(flight3);

        // Mock Flight 4
        FlightResponseDTO flight4 = new FlightResponseDTO();
        flight4.setOrigin("SYD");
        flight4.setDestination("BKK");
        flight4.setAwayDepartureTime("2021-11-03T11:35:00");
        flight4.setAwayArrivalTime("2021-11-03T21:50:00");
        flight4.setAirline("PR");
        flight4.setAwayDuration("PT14H15M");

        Segment segment7 = new Segment();
        segment7.setOrigin("SYD");
        segment7.setDestination("MNL");
        segment7.setDepartureTime("2021-11-03T11:35:00");
        segment7.setArrivalTime("2021-11-03T16:50:00");
        segment7.setAirline("PR");
        segment7.setFlightNumber("212");
        segment7.setAircraft("333");
        segment7.setDuration("PT8H15M");

        FareDetails fareDetails7 = new FareDetails();
        fareDetails7.setFareCabin("ECONOMY");
        fareDetails7.setFareClass("E");
        fareDetails7.setFareBasis("EOBAU");
        fareDetails7.setCheckedBags(List.of("25 KG"));
        segment7.setDetails(fareDetails7);

        Segment segment8 = new Segment();
        segment8.setOrigin("MNL");
        segment8.setDestination("BKK");
        segment8.setDepartureTime("2021-11-03T19:20:00");
        segment8.setArrivalTime("2021-11-03T21:50:00");
        segment8.setAirline("PR");
        segment8.setFlightNumber("732");
        segment8.setAircraft("320");
        segment8.setDuration("PT3H30M");

        FareDetails fareDetails8 = new FareDetails();
        fareDetails8.setFareCabin("ECONOMY");
        fareDetails8.setFareClass("E");
        fareDetails8.setFareBasis("EOBAU");
        fareDetails8.setCheckedBags(List.of("25 KG"));
        segment8.setDetails(fareDetails8);

        flight4.setDepartureSegments(List.of(segment7, segment8));

        PriceBreakdown priceBreakdown4 = new PriceBreakdown(255.00, 355.34, 0.00, 355.34, "EUR");
        flight4.setPriceBreakdown(priceBreakdown4);

        mockFlights.add(flight4);

        return mockFlights;
    }
}
