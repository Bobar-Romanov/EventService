package com.service.EventMicroservice.controller;

import com.service.EventMicroservice.exception.EventTimeConflictException;
import com.service.EventMicroservice.exception.HTTPRequestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(EventTimeConflictException.class)
    protected ResponseEntity<Object> handleEventTimeConflictException(EventTimeConflictException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Event time conflict");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }
    //
    @ExceptionHandler(HTTPRequestException.class)
    protected ResponseEntity<Object> handleHTTPRequestException(HTTPRequestException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "HTTP Request Exception");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Entity not found");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(error -> error.getPropertyPath().toString() + ": " + error.getMessage())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);

    }


}
