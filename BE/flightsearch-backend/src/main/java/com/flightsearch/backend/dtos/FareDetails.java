package com.flightsearch.backend.dtos;

import java.util.List;

public class FareDetails {
    private String fareCabin;
    private String fareClass;
    private String fareBasis;
    private List<String> checkedBags;

    public String getFareCabin() {
        return fareCabin;
    }

    public void setFareCabin(String fareCabin) {
        this.fareCabin = fareCabin;
    }

    public String getFareClass() {
        return fareClass;
    }

    public void setFareClass(String fareClass) {
        this.fareClass = fareClass;
    }

    public String getFareBasis() {
        return fareBasis;
    }

    public void setFareBasis(String fareBasis) {
        this.fareBasis = fareBasis;
    }

    public List<String> getCheckedBags() {
        return checkedBags;
    }

    public void setCheckedBags(List<String> checkedBags) {
        this.checkedBags = checkedBags;
    }
}
