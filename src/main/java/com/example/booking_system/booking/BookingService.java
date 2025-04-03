package com.example.booking_system.booking;


import com.example.booking_system.room.Room;
import com.example.booking_system.room.RoomRepository;
import com.example.booking_system.user.User;
import com.example.booking_system.user.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class BookingService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository){
        this.bookingRepository=bookingRepository;
        this.roomRepository=roomRepository;
    }

    public Booking createBooking(Booking booking){
        Room room = booking.getRoom();
        LocalDateTime start = booking.getStartTime();
        LocalDateTime end = booking.getEndTime();

        // Check for overlapping bookings
        List<Booking> conflicts = bookingRepository
                .findByRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(room, end, start);

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Room is already booked for the selected time range.");
        }

        return bookingRepository.save(booking);
    }

    public Booking createBookingFromRequest(BookingRequest request) {
        System.out.println("Saving booking for user ID: " + request.getUserId());
        System.out.println("Room ID: " + request.getRoomId());
        System.out.println("Start: " + request.getStartTime() + " | End: " + request.getEndTime());

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        User user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());

        System.out.println("Checking for conflicts...");
        List<Booking> conflicts = bookingRepository
                .findByRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(room, booking.getEndTime(), booking.getStartTime());

        System.out.println("Conflicts found: " + conflicts.size());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Room is already booked for the selected time range.");
        }

        Booking saved = bookingRepository.save(booking);
        System.out.println("✅ Booking saved: " + saved.getId());

        return saved;
    }



    public List<Booking> getBookingsByUser(Long userId){
        return bookingRepository.findByUserId(userId);
    }

    public void cancelBooking(Long bookingId){
        bookingRepository.deleteById(bookingId);
    }

    public Booking getBookingById(Long id){
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found."));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<BookingDTO> getAllBookingDTOs() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return bookingRepository.findAll().stream()
                .map(booking -> new BookingDTO(
                        booking.getId(),
                        booking.getUser() != null ? booking.getUser().getFullName() : "N/A",
                        booking.getUser() != null ? booking.getUser().getEmail() : "N/A",
                        booking.getRoom() != null ? booking.getRoom().getName() : "N/A",
                        booking.getRoom() != null ? booking.getRoom().getLocation() : "N/A",
                        booking.getStatus(),
                        booking.getStartTime() != null ? booking.getStartTime().format(fmt) : null,
                        booking.getEndTime() != null ? booking.getEndTime().format(fmt) : null,
                        booking.getCreatedAt() != null ? booking.getCreatedAt().format(fmt) : null
                ))
                .toList();
    }


    public List<BookingDTO> getBookingsByUserAsDTO(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        return bookingRepository.findByUser_Id(userId).stream()
                .map(booking -> {
                    String dynamicStatus = booking.getEndTime().isBefore(now) ? "COMPLETED" : booking.getStatus();

                    return new BookingDTO(
                            booking.getId(),
                            booking.getUser().getFullName(),
                            booking.getUser().getEmail(),
                            booking.getRoom().getName(),
                            booking.getRoom().getLocation(),
                            dynamicStatus,
                            booking.getStartTime().toString(),
                            booking.getEndTime().toString(),
                            booking.getCreatedAt().toString()
                    );
                })
                .toList();
    }




    public void updateExpiredBookingsToCompleted() {
        List<Booking> expired = bookingRepository.findByStatusAndEndTimeBefore("PENDING", LocalDateTime.now());

        for (Booking booking : expired) {
            booking.setStatus("COMPLETED");
            bookingRepository.save(booking);
        }

        System.out.println("✅ Auto-updated " + expired.size() + " expired bookings to COMPLETED.");
    }



    @Scheduled(fixedRate = 60000) // every 1 minute
    public void updateCompletedBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> pendingBookings = bookingRepository.findByStatus("PENDING");

        for (Booking booking : pendingBookings) {
            if (booking.getEndTime().isBefore(now)) {
                booking.setStatus("COMPLETED");
                bookingRepository.save(booking);
            }
        }
    }



}
