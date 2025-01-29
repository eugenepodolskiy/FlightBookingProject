package com.example.flightbookingproject.exception;

public class RateLimitExceededException extends AmadeusApiException {
    public RateLimitExceededException(String message) {
        super(message);
    }

    public RateLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}