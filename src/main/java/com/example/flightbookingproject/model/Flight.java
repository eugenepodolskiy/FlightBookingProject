package com.example.flightbookingproject.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private int stops;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<FlightSegment> segments;

    public Flight() {}

    public Flight(String flightNumber, String origin, String destination,
                  LocalDateTime departureTime, LocalDateTime arrivalTime,
                  double price, int stops, List<FlightSegment> segments) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.stops = stops;
        this.segments = segments;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStops() { return stops; }
    public void setStops(int stops) { this.stops = stops; }

    public List<FlightSegment> getSegments() { return segments; }
    public void setSegments(List<FlightSegment> segments) { this.segments = segments; }

    public Duration getTotalDuration() {
        if (segments == null || segments.isEmpty()) {
            return Duration.ZERO;
        }

        // Find the first segment's departure time and last segment's arrival time
        LocalDateTime firstDeparture = segments.get(0).getDepartureTime();
        LocalDateTime lastArrival = segments.get(segments.size() - 1).getArrivalTime();

        if (firstDeparture == null || lastArrival == null) {
            return Duration.ZERO; // Prevent NullPointerException
        }

        return Duration.between(firstDeparture, lastArrival);
    }

    // Calculate total layover time between segments
    public Duration getTotalLayoverTime() {
        if (segments == null || segments.size() < 2) {
            return Duration.ZERO; // No layover if there's only one segment
        }

        Duration totalLayover = Duration.ZERO;
        for (int i = 0; i < segments.size() - 1; i++) {
            LocalDateTime arrival = segments.get(i).getArrivalTime();
            LocalDateTime nextDeparture = segments.get(i + 1).getDepartureTime();
            totalLayover = totalLayover.plus(Duration.between(arrival, nextDeparture));
        }
        return totalLayover;
    }
}