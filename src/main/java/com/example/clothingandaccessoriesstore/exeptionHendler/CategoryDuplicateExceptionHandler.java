package com.example.clothingandaccessoriesstore.exeptionHendler;

import com.example.clothingandaccessoriesstore.exeption.CategoryDuplicateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryDuplicateExceptionHandler {
    @ExceptionHandler(CategoryDuplicateException.class)
    public ResponseEntity<String> handleCategoryDuplicate(CategoryDuplicateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Category duplicated");
    }
}
