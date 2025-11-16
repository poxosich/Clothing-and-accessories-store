package com.example.clothingandaccessoriesstore.exeptionHendler;

import com.example.clothingandaccessoriesstore.exeption.PasswordsNotDuplicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PasswordsNotDuplicateHandler {
    @ExceptionHandler(PasswordsNotDuplicate.class)
    public ResponseEntity<String> handlePasswordsNotDuplicate(PasswordsNotDuplicate passwordsNotDuplicate) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(passwordsNotDuplicate.getMessage());
    }
}
