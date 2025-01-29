package com.example.flightbookingproject.config;

import com.amadeus.Amadeus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfig {

    @Bean
    public Amadeus amadeus() {
        return Amadeus.builder("ZoyRur2PlZRP1Ax7dGmsCzTggzstublI", "KKmIB3qgOREzmTJT").build();
    }
}
