package com.example.flightbookingproject.service;

import com.example.flightbookingproject.model.*;
import com.example.flightbookingproject.repository.BookingRepository;
import com.example.flightbookingproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public Booking createBooking(String email, String flightNumber) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = new Booking(user, flightNumber, LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookings(String email, BookingStatus status) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return (status != null) ?
                bookingRepository.findByUserAndStatus(user, status) :
                bookingRepository.findByUser(user);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    public void markAsPaid(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setPaymentStatus(PaymentStatus.PAID);
        bookingRepository.save(booking);
    }
}