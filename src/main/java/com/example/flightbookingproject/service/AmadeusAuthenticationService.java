package com.example.flightbookingproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Service
public class AmadeusAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AmadeusAuthenticationService.class);

    @Value("${amadeus.apiKey}")
    private String apiKey;

    @Value("${amadeus.apiSecret}")
    private String apiSecret;

    @Value("${amadeus.api.url.token}")
    private String tokenUrl;

    @Autowired
    private RestTemplate restTemplate;

    private String accessToken;
    private Instant tokenExpiryTime;

    public void fetchAccessToken() {
        logger.info("Fetching new access token from Amadeus API");

        String credentials = "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(credentials, headers);
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            this.accessToken = root.path("access_token").asText();
            this.tokenExpiryTime = Instant.now().plusSeconds(root.path("expires_in").asInt());
            logger.info("Successfully fetched new access token");
        } catch (Exception e) {
            logger.error("Failed to parse access token", e);
            throw new RuntimeException("Failed to parse access token", e);
        }
    }

    public String getAccessToken() {
        if (accessToken == null || Instant.now().isAfter(tokenExpiryTime)) {
            fetchAccessToken();
        }
        return accessToken;
    }
}