package com.flightsearch.backend.dtos;

public class Segment {
    private String origin;
    private String destination;
    private String duration;
    private String departureTime;
    private String arrivalTime;
    private String airline;
    private String flightNumber;
    private String aircraft;
    private FareDetails details;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getFlighNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flighNumber) {
        this.flightNumber = flighNumber;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public FareDetails getDetails() {
        return details;
    }

    public void setDetails(FareDetails details) {
        this.details = details;
    }
}
