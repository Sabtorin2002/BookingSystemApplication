package com.example.booking_system.scheduler;

import com.example.booking_system.booking.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusScheduler {

    private final BookingService bookingService;

    public BookingStatusScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 60000) // every 60 seconds
    public void updateStatuses() {
        bookingService.updateExpiredBookingsToCompleted();
    }
}
