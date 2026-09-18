package com.example.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppointmentApplication {
    // Bootstraps the appointment booking REST API.
    public static void main(String[] args) {
        SpringApplication.run(AppointmentApplication.class, args);
    }
}
