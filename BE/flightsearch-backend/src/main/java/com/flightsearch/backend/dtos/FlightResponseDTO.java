package com.flightsearch.backend.dtos;

import java.util.List;

public class FlightResponseDTO {
    private String origin;
    private String destination;
    private String awayDuration;
    private String returnDuration;
    private String awayDepartureTime;
    private String awayArrivalTime;
    private String returnDepartureTime;
    private String returnArrivalTime;
    private String airline;
    private List<Segment> departureSegments;
    private List<Segment> returnSegments;
    private PriceBreakdown priceBreakdown;

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
    public String getAwayDuration() {
        return awayDuration;
    }

    public void setAwayDuration(String awayDuration) {
        this.awayDuration = awayDuration;
    }

    public String getReturnDuration() {
        return returnDuration;
    }

    public void setReturnDuration(String returnDuration) {
        this.returnDuration = returnDuration;
    }

    public PriceBreakdown getPriceBreakdown() {
        return priceBreakdown;
    }

    public void setPriceBreakdown(PriceBreakdown priceBreakdown) {
        this.priceBreakdown = priceBreakdown;
    }

    public String getAwayDepartureTime() {
        return awayDepartureTime;
    }

    public void setAwayDepartureTime(String awayDepartureTime) {
        this.awayDepartureTime = awayDepartureTime;
    }

    public String getAwayArrivalTime() {
        return awayArrivalTime;
    }

    public void setAwayArrivalTime(String awayArrivalTime) {
        this.awayArrivalTime = awayArrivalTime;
    }

    public String getReturnDepartureTime() {
        return returnDepartureTime;
    }

    public void setReturnDepartureTime(String returnDepartureTime) {
        this.returnDepartureTime = returnDepartureTime;
    }

    public String getReturnArrivalTime() {
        return returnArrivalTime;
    }

    public void setReturnArrivalTime(String returnArrivalTime) {
        this.returnArrivalTime = returnArrivalTime;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public List<Segment> getDepartureSegments() {
        return departureSegments;
    }

    public void setDepartureSegments(List<Segment> departureSegments) {
        this.departureSegments = departureSegments;
    }

    public List<Segment> getReturnSegments() {
        return returnSegments;
    }

    public void setReturnSegments(List<Segment> returnSegments) {
        this.returnSegments = returnSegments;
    }
}
