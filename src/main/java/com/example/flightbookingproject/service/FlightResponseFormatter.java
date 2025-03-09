package com.example.flightbookingproject.service;

import com.example.flightbookingproject.model.Flight;
import com.example.flightbookingproject.model.FlightSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightResponseFormatter {

    @Autowired
    private AirlineService airlineService;

    public String formatFlights(List<Flight> flights) {
        StringBuilder formattedResponse = new StringBuilder("Available Flights:\n\n");

        for (Flight flight : flights) {

            Duration totalDuration = flight.getTotalDuration();
            long hours = totalDuration.toHours();
            long minutes = totalDuration.toMinutesPart();

            formattedResponse.append("Flight from ").append(flight.getOrigin())
                    .append(" to ").append(flight.getDestination())
                    .append("\nPrice: $").append(flight.getPrice())
                    .append("\nTotal travel time: ").append(hours).append("h ").append(minutes).append("m\n")
                    .append("\nSegments:");



            List<FlightSegment> segments = flight.getSegments();
            for (int i = 0; i < segments.size(); i++) {
                FlightSegment segment = segments.get(i);

                String carrierCode = segment.getCarrierCode();
                String airlineName = airlineService.getAirlineName(carrierCode);
                String flightNumber = segment.getFlightNumber();
                String departureAirport = segment.getDepartureAirport();
                LocalDateTime departureTime = segment.getDepartureTime();
                String arrivalAirport = segment.getArrivalAirport();
                LocalDateTime arrivalTime = segment.getArrivalTime();
                int stops = segment.getStops(); // More accurate stop count per segment

                if (departureTime == null || arrivalTime == null) {
                    System.out.println("Warning: departureTime or arrivalTime is null for flight " + flightNumber);
                }

                formattedResponse.append("  Segment ").append(i + 1).append(":\n")
                        .append("    Airline: ").append(airlineName).append(" (").append(carrierCode).append(")\n")
                        .append("    Flight Number: ").append(flightNumber).append("\n")
                        .append("    Departure: ").append(departureAirport)
                        .append(" at ").append(formatDateTime(departureTime)).append("\n")
                        .append("    Arrival: ").append(arrivalAirport)
                        .append(" at ").append(formatDateTime(arrivalTime)).append("\n")
                        .append("    Duration: ").append(formatDuration(Duration.between(departureTime, arrivalTime))).append("\n")
                        .append("    Stops: ").append(stops).append("\n");

                // Display layover time if another segment exists
                if (i < segments.size() - 1) {
                    FlightSegment nextSegment = segments.get(i + 1);
                    Duration layoverTime = Duration.between(arrivalTime, nextSegment.getDepartureTime());

                    formattedResponse.append("    Layover at ").append(arrivalAirport)
                            .append(": ").append(formatDuration(layoverTime)).append("\n");
                }
            }
            formattedResponse.append("\n");
        }

        return formattedResponse.toString();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' HH:mm (h:mm a)");
        return dateTime.format(formatter);
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return hours + "h " + minutes + "m";
    }
}