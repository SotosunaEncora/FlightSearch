package com.flightsearch.backend.dtos;

public class AirportCityDTO {
    private final String code; // IATA Code
    private final String name; // Airport Name
    private final String cityName;

    public AirportCityDTO(String code, String name, String cityName) {
        this.code = code;
        this.name = name;
        this.cityName = cityName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCityName() {
        return cityName;
    }
}
