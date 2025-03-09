package com.example.flightbookingproject.controller;

import com.example.flightbookingproject.model.Booking;
import com.example.flightbookingproject.model.BookingStatus;
import com.example.flightbookingproject.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam String flightNumber) {
        Booking booking = bookingService.createBooking(userDetails.getUsername(), flightNumber);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam(required = false) BookingStatus status) {
        List<Booking> bookings = bookingService.getBookings(userDetails.getUsername(), status);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking canceled successfully.");
    }

    @PutMapping("/{bookingId}/pay")
    public ResponseEntity<String> markBookingAsPaid(@PathVariable Long bookingId) {
        bookingService.markAsPaid(bookingId);
        return ResponseEntity.ok("Payment successful.");
    }
}
