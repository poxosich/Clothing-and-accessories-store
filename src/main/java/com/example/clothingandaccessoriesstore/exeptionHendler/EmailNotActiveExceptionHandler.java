package com.example.clothingandaccessoriesstore.exeptionHendler;

import com.example.clothingandaccessoriesstore.exeption.EmailNotActiveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmailNotActiveExceptionHandler {
    @ExceptionHandler(EmailNotActiveException.class)
    public ResponseEntity<String> handleEmailNotActive(EmailNotActiveException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
