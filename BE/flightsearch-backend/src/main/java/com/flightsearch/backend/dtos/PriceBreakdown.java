package com.flightsearch.backend.dtos;

public class PriceBreakdown {
    private double base;
    private String currency;
    private double total;
    private double fees;
    private double pricePerTraveler;

    public PriceBreakdown() {}

    public PriceBreakdown(double base, double total, double fees, double pricePerTraveler, String currency) {
        this.base = base;
        this.total = total;
        this.fees = fees;
        this.pricePerTraveler = pricePerTraveler;
        this.currency = currency;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public double getPricePerTraveler() {
        return pricePerTraveler;
    }

    public void setPricePerTraveler(double pricePerTraveler) {
        this.pricePerTraveler = pricePerTraveler;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}