package com.example.flightbookingproject.repository;

import com.example.flightbookingproject.model.Booking;
import com.example.flightbookingproject.model.BookingStatus;
import com.example.flightbookingproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
}
