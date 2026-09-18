package com.example.appointment.service;

import com.example.appointment.dto.AppointmentRequest;
import com.example.appointment.dto.AppointmentResponse;
import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import com.example.appointment.entity.User;
import com.example.appointment.exception.ResourceNotFoundException;
import com.example.appointment.exception.SlotAlreadyBookedException;
import com.example.appointment.repository.AppointmentRepository;
import com.example.appointment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
// Coordinates appointment business rules and database transactions.
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setDoctorName(request.getDoctorName());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> findAvailable(String doctorName, LocalDate appointmentDate, Pageable pageable) {
        return appointmentRepository.findAvailable(doctorName, appointmentDate, AppointmentStatus.AVAILABLE, pageable)
                .map(AppointmentResponse::from);
    }

    @Transactional
    public AppointmentResponse book(Long id, String email) {
        Appointment appointment = getAppointment(id);
        if (appointment.getStatus() != AppointmentStatus.AVAILABLE) {
            throw new SlotAlreadyBookedException("Appointment slot is already booked");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        appointment.setUser(user);
        appointment.setStatus(AppointmentStatus.BOOKED);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> findMine(String email, Pageable pageable) {
        return appointmentRepository.findByUserEmail(email, pageable).map(AppointmentResponse::from);
    }

    @Transactional
    public AppointmentResponse cancel(Long id, String email) {
        Appointment appointment = getAppointment(id);
        if (appointment.getUser() == null || !appointment.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Appointment not found for this user");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    private Appointment getAppointment(Long id) {
        return appointmentRepository.findWithLockById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }


}
