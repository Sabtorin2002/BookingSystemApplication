package com.example.booking_system.booking;

public class BookingDTO {
    private Long id;
    private String userFullName;
    private String userEmail;
    private String roomName;
    private String roomLocation;
    private String status;
    private String startTime;
    private String endTime;
    private String createdAt;

    public BookingDTO(Long id, String userFullName, String userEmail, String roomName, String roomLocation,
                      String status, String startTime, String endTime, String createdAt) {
        this.id = id;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.roomName = roomName !=null ? roomName : "Unknown";
        this.roomLocation = roomLocation;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getUserFullName() { return userFullName; }
    public String getUserEmail() { return userEmail; }
    public String getRoomName() { return roomName; }
    public String getRoomLocation() { return roomLocation; }
    public String getStatus() { return status; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getCreatedAt() { return createdAt; }
}
