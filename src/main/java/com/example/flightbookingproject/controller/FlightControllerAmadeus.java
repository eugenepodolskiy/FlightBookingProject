package com.example.flightbookingproject.controller;

import com.example.flightbookingproject.model.Flight;
import com.example.flightbookingproject.service.AmadeusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightControllerAmadeus {

    @Autowired
    private AmadeusService amadeusService;

    @GetMapping("/search")
    public String searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String departureDate,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filterBy,
            @RequestParam(required = false) String filterValue,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer maxStops) {

        List<Flight> flights = amadeusService.searchFlights(
                origin, destination, departureDate, adults, page, size, sortBy, filterBy, filterValue, minPrice, maxPrice, maxStops
        );

        System.out.println("[DEBUG] Returning " + flights.size() + " flights");
        return flights.isEmpty() ? "No flights found." : amadeusService.formatFlights(flights);
    }
}