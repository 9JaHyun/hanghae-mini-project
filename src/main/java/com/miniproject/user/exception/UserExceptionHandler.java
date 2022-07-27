package com.miniproject.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {
      "com.miniproject.user.controller",
      "com.miniproject.email",
      "com.miniproject.config.security"})
public class UserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> illegalStateException(IllegalStateException e) {
        return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> usernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(e.getMessage());
    }
}
