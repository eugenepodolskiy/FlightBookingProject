package com.example.flightbookingproject.model;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<FlightSegment> segments;



    // Add the 'stops' field
    private int stops; // Number of stops (e.g., 0 for non-stop)

    // Default constructor (required by JPA)
    public Flight() {
    }

    // All-arguments constructor (optional)
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

    // Getters and setters for all fields (including stops)
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
    public int getStops() { return stops; } // Add this getter
    public void setStops(int stops) { this.stops = stops; } // Add this setter

    public List<FlightSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<FlightSegment> segments) {
        this.segments = segments;
    }
}
