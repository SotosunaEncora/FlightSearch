package com.flightsearch.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FlightSearchBackEndApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // This test will fail if the application context cannot start
        assertNotNull(applicationContext);
    }

    // You can add more integration tests here
    // For example, you might want to test that all your controllers are properly initialized:

    @Test
    void controllersAreInitialized() {
        assertNotNull(applicationContext.getBean(com.flightsearch.backend.controllers.TestController.class));
        assertNotNull(applicationContext.getBean(com.flightsearch.backend.controllers.FlightsController.class));
        assertNotNull(applicationContext.getBean(com.flightsearch.backend.controllers.AirportsController.class));
    }

    // You can also add tests to verify that your custom beans are properly configured:

    @Test
    void amadeusClientIsInitialized() {
        assertNotNull(applicationContext.getBean(com.flightsearch.backend.clients.AmadeusClient.class));
    }
}