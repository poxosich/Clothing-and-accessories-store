package com.example.clothingandaccessoriesstore.exeptionHendler;

import com.example.clothingandaccessoriesstore.exeption.UserDublinException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserDublinExceptionHandler {
    @ExceptionHandler(UserDublinException.class)
    public ResponseEntity<String> handleUserDublinException(UserDublinException userDublinException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDublinException.getMessage());
    }
}
