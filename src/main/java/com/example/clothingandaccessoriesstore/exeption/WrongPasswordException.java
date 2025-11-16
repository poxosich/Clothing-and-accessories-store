package com.example.clothingandaccessoriesstore.exeption;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
