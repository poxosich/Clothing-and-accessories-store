package com.example.clothingandaccessoriesstore.exeption;

public class CategoryDuplicateException extends RuntimeException {
    public CategoryDuplicateException(String message) {
        super(message);
    }
}
