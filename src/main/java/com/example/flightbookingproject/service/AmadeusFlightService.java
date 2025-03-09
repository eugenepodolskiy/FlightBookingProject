package com.example.flightbookingproject.service;

import com.example.flightbookingproject.model.Flight;
import com.example.flightbookingproject.model.FlightSegment;
import com.example.flightbookingproject.repository.FlightRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmadeusFlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AmadeusCityToIataService amadeusCityToIataService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AmadeusAuthenticationService authenticationService;

    private static final String FLIGHT_OFFERS_URL = "https://test.api.amadeus.com/v2/shopping/flight-offers";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public List<Flight> fetchAndSaveFlights(String origin, String destination, String departureDate, int adults) {

        String originCode = isIataCode(origin) ? origin : amadeusCityToIataService.getIataCode(origin);
        String destinationCode = isIataCode(destination) ? destination : amadeusCityToIataService.getIataCode(destination);

        if (originCode == null || destinationCode == null) {
            throw new IllegalArgumentException("Invalid input: Unable to determine IATA code for " + origin + " or " + destination);
        }

        // Fetch flights from Amadeus API
        List<JsonNode> flights = fetchFlightsFromAmadeus(originCode, destinationCode, departureDate, adults);
        System.out.println("[DEBUG] Fetched " + flights.size() + " flights from Amadeus API");

        // Convert and save flights
        List<Flight> savedFlights = flights.stream()
                .map(this::convertToFlightEntity)
                .map(flight -> {
                    Flight saved = flightRepository.save(flight);
                    System.out.println("[DEBUG] Saved flight: " + saved.getFlightNumber());
                    return saved;
                })
                .collect(Collectors.toList());

        return savedFlights;
    }

    private List<JsonNode> fetchFlightsFromAmadeus(String origin, String destination, String departureDate, int adults) {
        String url = FLIGHT_OFFERS_URL + "?originLocationCode=" + origin +
                "&destinationLocationCode=" + destination +
                "&departureDate=" + departureDate +
                "&adults=" + adults;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authenticationService.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            System.out.println("[DEBUG] Raw API Response: " + response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode dataNode = rootNode.get("data");

            if (dataNode != null && dataNode.isArray()) {
                return dataNode.findParents("itineraries");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch flights: " + e.getMessage());
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private Flight convertToFlightEntity(JsonNode flightNode) {
        Flight flight = new Flight();

        // Extract origin and destination from the first and last segments
        JsonNode segmentsNode = flightNode.path("itineraries").get(0).path("segments");
        String origin = segmentsNode.get(0).path("departure").path("iataCode").asText();
        String destination = segmentsNode.get(segmentsNode.size() - 1).path("arrival").path("iataCode").asText();
        flight.setOrigin(origin);
        flight.setDestination(destination);

        // Extract price
        flight.setPrice(flightNode.path("price").path("total").asDouble());

        // Parse segments
        List<FlightSegment> segments = new ArrayList<>();

        for (int i = 0; i < segmentsNode.size(); i++) {
            JsonNode segmentNode = segmentsNode.get(i);
            FlightSegment segment = new FlightSegment();

            segment.setDepartureAirport(segmentNode.path("departure").path("iataCode").asText());
            segment.setArrivalAirport(segmentNode.path("arrival").path("iataCode").asText());
            segment.setDepartureTime(LocalDateTime.parse(segmentNode.path("departure").path("at").asText(), DateTimeFormatter.ISO_DATE_TIME));
            segment.setArrivalTime(LocalDateTime.parse(segmentNode.path("arrival").path("at").asText(), DateTimeFormatter.ISO_DATE_TIME));
            segment.setCarrierCode(segmentNode.path("carrierCode").asText());
            segment.setFlightNumber(segmentNode.path("number").asText());
            segment.setFlight(flight); // Link segment to the flight

            // Calculate stops: If it's not the last segment, set stops to remaining segments
            int stops = (i < segmentsNode.size() - 1) ? 1 : 0;
            segment.setStops(stops);

            segments.add(segment);
        }

        flight.setSegments(segments);



        return flight;
    }

    // Helper method to check if the input is already an IATA code
    private boolean isIataCode(String input) {
        return input != null && input.matches("^[A-Z]{3}$"); // Matches 3 uppercase letters (e.g., MUC, LCY)
    }
}