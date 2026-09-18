package com.example.appointment.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import com.example.appointment.entity.Role;
import com.example.appointment.entity.User;
import com.example.appointment.repository.AppointmentRepository;
import com.example.appointment.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DemoUserInitializer {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    // Keeps demo database users aligned with the in-memory security users.
    @Bean
    CommandLineRunner seedDemoUsers() {
        return args -> {
            User admin = createIfMissing("Admin", "admin@example.com", Role.ADMIN);
            User regularUser = createIfMissing("Demo User", "user@example.com", Role.USER);

            seedAppointments(admin, regularUser);
        };
    }

    private User createIfMissing(String name, String email, Role role) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setRole(role);
                    return userRepository.save(user);
                });
    }

    private void seedAppointments(User admin, User regularUser) {
        LocalDate today = LocalDate.now();

        List<AppointmentSeed> seeds = List.of(
                new AppointmentSeed("Dr. Smith", today.plusDays(1), LocalTime.of(9, 0), LocalTime.of(9, 30), AppointmentStatus.AVAILABLE, null),
                new AppointmentSeed("Dr. Smith", today.plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 30), AppointmentStatus.BOOKED, regularUser),
                new AppointmentSeed("Dr. Patel", today.plusDays(2), LocalTime.of(11, 0), LocalTime.of(11, 30), AppointmentStatus.AVAILABLE, null),
                new AppointmentSeed("Dr. Patel", today.plusDays(2), LocalTime.of(14, 0), LocalTime.of(14, 30), AppointmentStatus.CANCELLED, regularUser),
                new AppointmentSeed("Dr. Johnson", today.plusDays(3), LocalTime.of(8, 30), LocalTime.of(9, 0), AppointmentStatus.AVAILABLE, null),
                new AppointmentSeed("Dr. Johnson", today.plusDays(3), LocalTime.of(15, 0), LocalTime.of(15, 30), AppointmentStatus.BOOKED, regularUser),
                new AppointmentSeed("Dr. Lee", today.plusDays(4), LocalTime.of(12, 0), LocalTime.of(12, 30), AppointmentStatus.AVAILABLE, null),
                new AppointmentSeed("Dr. Lee", today.plusDays(5), LocalTime.of(9, 0), LocalTime.of(9, 45), AppointmentStatus.AVAILABLE, null),
                new AppointmentSeed("Dr. Chen", today.plusDays(6), LocalTime.of(10, 30), LocalTime.of(11, 0), AppointmentStatus.AVAILABLE, null),
                new AppointmentSeed("Dr. Chen", today.plusDays(7), LocalTime.of(13, 0), LocalTime.of(13, 45), AppointmentStatus.BOOKED, admin)
        );

        for (AppointmentSeed seed : seeds) {
            boolean exists = appointmentRepository.findAll().stream()
                    .anyMatch(existing -> existing.getDoctorName().equals(seed.doctorName())
                            && existing.getAppointmentDate().equals(seed.appointmentDate())
                            && existing.getStartTime().equals(seed.startTime())
                            && existing.getEndTime().equals(seed.endTime()));

            if (exists) {
                continue;
            }

            Appointment appointment = new Appointment();
            appointment.setDoctorName(seed.doctorName());
            appointment.setAppointmentDate(seed.appointmentDate());
            appointment.setStartTime(seed.startTime());
            appointment.setEndTime(seed.endTime());
            appointment.setStatus(seed.status());
            appointment.setUser(seed.user());
            appointmentRepository.save(appointment);
        }
    }

    private record AppointmentSeed(String doctorName,
                                   LocalDate appointmentDate,
                                   LocalTime startTime,
                                   LocalTime endTime,
                                   AppointmentStatus status,
                                   User user) {
    }
}
