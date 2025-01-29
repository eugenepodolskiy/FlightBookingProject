package com.example.flightbookingproject.repository;

import com.example.flightbookingproject.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginAndDestination(String origin, String destination);
}