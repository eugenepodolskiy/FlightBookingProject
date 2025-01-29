package com.example.flightbookingproject.service;

import com.example.flightbookingproject.exception.AirlineNotFoundException;
import com.example.flightbookingproject.exception.RateLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AirlineService {

    private static final Logger logger = LoggerFactory.getLogger(AirlineService.class);

    @Autowired
    private AmadeusAuthenticationService authenticationService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${amadeus.api.url.airlines}")
    private String airlineApiUrl;

    @Cacheable(value = "airlines", key = "#carrierCode")
    @Retryable(
            value = {RateLimitExceededException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public String getAirlineName(String carrierCode) {
        logger.info("Fetching airline name for carrier code: {}", carrierCode);
        String url = airlineApiUrl + "?airlineCodes=" + carrierCode;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authenticationService.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = new ObjectMapper().readTree(response.getBody());

            if (root.path("data").isEmpty()) {
                logger.warn("Airline not found for carrier code: {}", carrierCode);
                throw new AirlineNotFoundException("Airline not found for code: " + carrierCode);
            }

            String airlineName = root.path("data").get(0).path("commonName").asText();
            logger.info("Successfully fetched airline name: {} for carrier code: {}", airlineName, carrierCode);
            return airlineName;
        } catch (Exception e) {
            if (e instanceof org.springframework.web.client.HttpClientErrorException &&
                    ((org.springframework.web.client.HttpClientErrorException) e).getStatusCode().value() == 429) {
                logger.warn("Rate limit exceeded for carrier code: {}", carrierCode);
                throw new RateLimitExceededException("Rate limit exceeded for code: " + carrierCode, e);
            } else {
                logger.error("Failed to fetch airline name for carrier code: {}", carrierCode, e);
                throw new RuntimeException("Failed to fetch airline name for code: " + carrierCode, e);
            }
        }
    }

    @Recover
    public String recover(RateLimitExceededException e, String carrierCode) {
        logger.error("Max retry attempts reached for carrier code: {}", carrierCode);
        throw new RuntimeException("Max retry attempts reached. Failed to fetch airline name for code: " + carrierCode, e);
    }
}