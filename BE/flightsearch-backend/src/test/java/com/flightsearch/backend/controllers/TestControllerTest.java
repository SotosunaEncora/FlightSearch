package com.flightsearch.backend.controllers;

import com.flightsearch.backend.clients.AmadeusClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestControllerTest {

    @Mock
    private AmadeusClient amadeusClient;

    @InjectMocks
    private TestController testController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConnection() {
        // Arrange
        String expectedResponse = "Connection successful";
        when(amadeusClient.testConnection()).thenReturn(expectedResponse);

        // Act
        String result = testController.testConnection();

        // Assert
        assertEquals(expectedResponse, result);
        verify(amadeusClient).testConnection();
    }

    @Test
    void testConnectionFailure() {
        // Arrange
        when(amadeusClient.testConnection()).thenThrow(new RuntimeException("Connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> testController.testConnection());
        verify(amadeusClient).testConnection();
    }

    // Add more tests as needed for other methods in TestController
}
