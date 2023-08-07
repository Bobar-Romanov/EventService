package com.service.EventMicroservice.exception;

public class EventTimeConflictException extends RuntimeException {
    public EventTimeConflictException(String message) {
        super(message);
    }
}
