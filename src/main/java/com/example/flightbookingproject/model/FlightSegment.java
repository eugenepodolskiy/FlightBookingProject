package com.example.flightbookingproject.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class FlightSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String carrierCode;
    private String flightNumber;
    private int stops; // Number of stops per segment
    private String operatingAirline; // If different from carrierCode
    private String aircraftType; // If available from API

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public FlightSegment() {}

    public FlightSegment(String departureAirport, String arrivalAirport,
                         LocalDateTime departureTime, LocalDateTime arrivalTime,
                         String carrierCode, String flightNumber, int stops,
                         String operatingAirline, String aircraftType) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.carrierCode = carrierCode;
        this.flightNumber = flightNumber;
        this.stops = stops;
        this.operatingAirline = operatingAirline;
        this.aircraftType = aircraftType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(String departureAirport) { this.departureAirport = departureAirport; }

    public String getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(String arrivalAirport) { this.arrivalAirport = arrivalAirport; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getCarrierCode() { return carrierCode; }
    public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public int getStops() { return stops; }
    public void setStops(int stops) { this.stops = stops; }

    public String getOperatingAirline() { return operatingAirline; }
    public void setOperatingAirline(String operatingAirline) { this.operatingAirline = operatingAirline; }

    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    // Calculate duration of the flight segment
    public Duration getSegmentDuration() {
        return Duration.between(departureTime, arrivalTime);
    }
}