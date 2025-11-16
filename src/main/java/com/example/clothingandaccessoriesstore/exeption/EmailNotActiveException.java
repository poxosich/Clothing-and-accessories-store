package com.example.clothingandaccessoriesstore.exeption;

public class EmailNotActiveException extends RuntimeException {
    public EmailNotActiveException(String message) {
        super(message);
    }
}
