package com.example.appointment.exception;

// Raised when a requested resource does not exist or is not owned by the caller.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
