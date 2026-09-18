package com.example.appointment.repository;

import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.time.LocalDate;
import jakarta.persistence.LockModeType;

// Database access for appointment search, booking, and ownership queries.
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Appointment> findWithLockById(Long id);

    @Query("select a from Appointment a where (:doctorName is null or lower(a.doctorName) like lower(concat('%', :doctorName, '%'))) "
            + "and (:appointmentDate is null or a.appointmentDate = :appointmentDate) and a.status = :status")
    Page<Appointment> findAvailable(@Param("doctorName") String doctorName,
                                    @Param("appointmentDate") LocalDate appointmentDate,
                                    @Param("status") AppointmentStatus status,
                                    Pageable pageable);

    Page<Appointment> findByUserEmail(String email, Pageable pageable);
}
