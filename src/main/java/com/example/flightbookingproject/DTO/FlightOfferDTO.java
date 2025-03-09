package com.example.flightbookingproject.DTO;

import java.util.Objects;

public class FlightOfferDTO {
    private String origin;
    private String destination;
    private String price;

    public FlightOfferDTO(String origin, String destination, String price) {
        this.origin = origin;
        this.destination = destination;
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightOfferDTO that = (FlightOfferDTO) o;
        return Objects.equals(origin, that.origin) &&
                Objects.equals(destination, that.destination) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, price);
    }
}
