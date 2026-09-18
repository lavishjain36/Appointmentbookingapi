package com.example.appointment.dto;

import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

// Stable API response shape returned by appointment endpoints.
public record AppointmentResponse(
        Long id,
        String doctorName,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status,
        String bookedBy
) {
    // Maps the entity while a service transaction is still open for lazy relations.
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctorName(),
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getStatus(),
                appointment.getUser() == null ? null : appointment.getUser().getEmail()
        );
    }
}
