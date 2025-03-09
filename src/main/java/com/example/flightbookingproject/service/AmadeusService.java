package com.example.flightbookingproject.service;

import com.example.flightbookingproject.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmadeusService {

    @Autowired
    private AmadeusFlightService flightService;

    @Autowired
    private FlightResponseFormatter responseFormatter;

    public List<Flight> searchFlights(String origin, String destination, String departureDate, int adults,
                                      int page, int size, String sortBy, String filterBy, String filterValue,
                                      Double minPrice, Double maxPrice, Integer maxStops) {
        // Fetch flights from the database
        List<Flight> allFlights = flightService.fetchAndSaveFlights(origin, destination, departureDate, adults);
        System.out.println("[DEBUG] Total flights fetched: " + allFlights.size());

        // Apply filters
        if (filterBy != null && filterValue != null) {
            switch (filterBy.toLowerCase()) {
                case "price":
                    double maxPriceFilter = Double.parseDouble(filterValue);
                    allFlights = allFlights.stream()
                            .filter(flight -> flight.getPrice() <= maxPriceFilter)
                            .collect(Collectors.toList());
                    break;
                case "airline":
                    allFlights = allFlights.stream()
                            .filter(flight -> flight.getFlightNumber().startsWith(filterValue))
                            .collect(Collectors.toList());
                    break;
            }
        }

        if (minPrice != null && maxPrice != null) {
            allFlights = allFlights.stream()
                    .filter(flight -> flight.getPrice() >= minPrice && flight.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // Apply sorting
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "price":
                    allFlights.sort(Comparator.comparing(Flight::getPrice));
                    break;
                case "departure":
                    allFlights.sort(Comparator.comparing(Flight::getDepartureTime));
                    break;
            }
        }

        // Apply pagination
        int start = page * size;
        int end = Math.min(start + size, allFlights.size());
        return allFlights.subList(start, end);
    }

    public String formatFlights(List<Flight> flights) {
        return responseFormatter.formatFlights(flights);
    }
}
