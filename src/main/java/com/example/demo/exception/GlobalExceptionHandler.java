package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (value = RuntimeException.class)
    ResponseEntity <String> handlingRuntimeException (RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
