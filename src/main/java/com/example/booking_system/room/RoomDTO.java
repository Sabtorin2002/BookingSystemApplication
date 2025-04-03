package com.example.booking_system.room;

public class RoomDTO {
    private Long id;
    private String name;
    private String description;
    private int capacity;
    private String location;
    private boolean isAvailable;

    public RoomDTO(Long id, String name, String description, int capacity, String location, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.location = location;
        this.isAvailable = isAvailable;
    }

    // Getters only (or setters if needed)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}

