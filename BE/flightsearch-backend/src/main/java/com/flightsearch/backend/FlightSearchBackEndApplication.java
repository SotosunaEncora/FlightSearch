package com.flightsearch.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FlightSearchBackEndApplication {

	public static void main(String[] args) {
		System.out.println("Application is starting...");

		SpringApplication.run(FlightSearchBackEndApplication.class, args);
	}

}
