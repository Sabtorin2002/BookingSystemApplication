package com.example.booking_system.booking;


import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        System.out.println("📩 Received booking request: "
                + "User ID = " + request.getUserId()
                + ", Room ID = " + request.getRoomId()
                + ", Start = " + request.getStartTime()
                + ", End = " + request.getEndTime());

        try {
            Booking created = bookingService.createBookingFromRequest(request);
            return ResponseEntity.ok(created);

        } catch (IllegalStateException e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookingDTOs();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBookingsByUser(@PathVariable Long userId){
        List<BookingDTO> bookings = bookingService.getBookingsByUserAsDTO(userId);
        return ResponseEntity.ok(bookings);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id){
        try{
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        }catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id){
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking was cancelled.");
    }

    @GetMapping("/update-statuses")
    public ResponseEntity<?> triggerStatusUpdate() {
        bookingService.updateExpiredBookingsToCompleted();
        return ResponseEntity.ok("Statuses updated.");
    }


}
