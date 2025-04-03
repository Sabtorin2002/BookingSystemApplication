package com.example.booking_system.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
