package com.example.appointment.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
// Request body used by admins to create an available appointment slot.
public class AppointmentRequest {
    @NotBlank
    private String doctorName;

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    // Bean Validation calls this derived property after JSON values are bound.
    @AssertTrue(message = "endTime must be after startTime")
    public boolean isEndTimeAfterStartTime() {
        return startTime != null && endTime != null && endTime.isAfter(startTime);
    }
}
