package com.example.appointment.controller;

import com.example.appointment.dto.AppointmentRequest;
import com.example.appointment.dto.AppointmentResponse;
import com.example.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments")
@SecurityRequirement(name = "basicAuth")
// REST endpoints for creating, finding, booking, and cancelling appointments.
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an available appointment slot")
    public AppointmentResponse create(@Valid @RequestBody AppointmentRequest request) {
        return appointmentService.create(request);
    }

    @GetMapping
    @Operation(summary = "View all appointments")
    public Page<AppointmentResponse> findAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size) {
        return appointmentService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/available")
    @Operation(summary = "Find available appointment slots")
    public Page<AppointmentResponse> findAvailable(
            @RequestParam(required = false) String doctorName,
            @RequestParam(required = false) LocalDate appointmentDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return appointmentService.findAvailable(doctorName, appointmentDate, PageRequest.of(page, size));
    }

    @PostMapping("/{id}/book")
    @Operation(summary = "Book an available appointment")
    public AppointmentResponse book(@PathVariable Long id, Authentication authentication) {
        return appointmentService.book(id, authentication.getName());
    }

    @GetMapping("/my")
    @Operation(summary = "View my appointments")
    public Page<AppointmentResponse> findMine(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size,
                                              Authentication authentication) {
        return appointmentService.findMine(authentication.getName(), PageRequest.of(page, size));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel my appointment")
    public AppointmentResponse cancel(@PathVariable Long id, Authentication authentication) {
        return appointmentService.cancel(id, authentication.getName());
    }
}
