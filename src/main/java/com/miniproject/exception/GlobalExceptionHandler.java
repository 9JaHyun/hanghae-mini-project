package com.miniproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentExceptionHandler(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentExceptionHandler(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(e.getMessage());
    }
}
