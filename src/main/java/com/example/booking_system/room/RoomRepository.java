package com.example.booking_system.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name); //is used to represent a value that might or might not be present.

    //Why use Optional?
    //Instead of returning null when a room isn't found (which can lead to NullPointerException if not handled carefully),
    // Optional forces the caller to explicitly handle the case when a value is absent.
    List<Room> findByIsAvailableTrue();
}
