package com.example.booking_system.booking;

import com.example.booking_system.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);
    List<Booking> findByRoomAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Room room,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
    //the function from above check for overlapping bookings

    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByStatusAndEndTimeBefore(String status, LocalDateTime time);
    List<Booking> findByStatus(String status);



}
