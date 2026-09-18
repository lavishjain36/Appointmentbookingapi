package com.example.appointment.exception;

// Raised when a user tries to book an appointment that is no longer available.
public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
