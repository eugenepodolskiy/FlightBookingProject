package com.example.flightbookingproject.exception;

public class AirlineNotFoundException extends AmadeusApiException {
    public AirlineNotFoundException(String message) {
        super(message);
    }

    public AirlineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}