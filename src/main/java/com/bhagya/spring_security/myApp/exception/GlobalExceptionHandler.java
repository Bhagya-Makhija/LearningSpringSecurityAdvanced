package com.bhagya.spring_security.myApp.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bhagya.spring_security.myApp.dto.BasicAuthResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BasicAuthResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BasicAuthResponse("User registration failed: Username or email already exists", null));
                // .body(new AuthResponse("User registration failed: "+e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BasicAuthResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BasicAuthResponse(e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicAuthResponse> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BasicAuthResponse("An unexpected error occurred: " + e.getMessage(), null));
    }
}