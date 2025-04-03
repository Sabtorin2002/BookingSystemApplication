package com.example.booking_system.configuration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class ConfigHomeController {

    @GetMapping
    public String home() {
        return "Welcome to the Booking System!";
    }
}
