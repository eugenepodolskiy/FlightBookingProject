package com.example.flightbookingproject.service;

import com.example.flightbookingproject.model.Flight;
import com.example.flightbookingproject.model.FlightSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightResponseFormatter {

    @Autowired
    private AirlineService airlineService;

    public String formatFlights(List<Flight> flights) {
        StringBuilder formattedResponse = new StringBuilder("Available Flights:\n\n");

        for (Flight flight : flights) {
            formattedResponse.append("Flight from ").append(flight.getOrigin())
                    .append(" to ").append(flight.getDestination())
                    .append("\nPrice: $").append(flight.getPrice())
                    .append("\nSegments:\n");

            for (FlightSegment segment : flight.getSegments()) {
                formattedResponse.append("  - ").append(segment.getDepartureAirport())
                        .append(" → ").append(segment.getArrivalAirport())
                        .append(" (Flight ").append(segment.getCarrierCode())
                        .append(segment.getFlightNumber()).append(")\n")
                        .append("    Departure: ").append(segment.getDepartureTime())
                        .append("\n    Arrival: ").append(segment.getArrivalTime())
                        .append("\n");
            }
            formattedResponse.append("\n");
        }

        return formattedResponse.toString();
    }
}