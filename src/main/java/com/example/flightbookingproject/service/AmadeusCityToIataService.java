package com.example.flightbookingproject.service;

import com.example.flightbookingproject.exception.RateLimitExceededException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AmadeusCityToIataService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AmadeusAuthenticationService authenticationService;

    private static final String LOCATION_SEARCH_URL = "https://test.api.amadeus.com/v1/reference-data/locations";

    @Retryable(
            value = {RateLimitExceededException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public String getIataCode(String city) {
        String url = LOCATION_SEARCH_URL + "?subType=CITY&keyword=" + city;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authenticationService.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode dataNode = rootNode.get("data");

            if (dataNode != null && dataNode.isArray() && dataNode.size() > 0) {
                return dataNode.get(0).path("iataCode").asText();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch IATA code for city: " + city + " - " + e.getMessage());
        }
        return null;
    }
}
