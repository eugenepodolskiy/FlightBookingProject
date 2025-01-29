package com.example.flightbookingproject.service;

import com.example.flightbookingproject.model.Booking;
import com.example.flightbookingproject.model.Flight;
import com.example.flightbookingproject.model.User;
import com.example.flightbookingproject.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public Booking createBooking(User user, Flight flight, double totalPrice) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus("CONFIRMED");
        booking.setTotalPrice(totalPrice);
        return bookingRepository.save(booking);
    }
}
