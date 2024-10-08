package com.flightsearch.backend.controllers;

import com.flightsearch.backend.clients.AmadeusClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final AmadeusClient amadeusClient;

    @Autowired
    public TestController(AmadeusClient amadeusClient) {
        this.amadeusClient = amadeusClient;
    }

    @GetMapping
    public String test() {
        return "Backend is working";
    }

    @GetMapping("/connectionTest")
    public String testConnection() {
        System.out.println("Controller Called!!!");
        return amadeusClient.testConnection();
    }
}
