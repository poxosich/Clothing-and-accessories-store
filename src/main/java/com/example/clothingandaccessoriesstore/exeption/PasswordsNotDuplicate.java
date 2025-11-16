package com.example.clothingandaccessoriesstore.exeption;

public class PasswordsNotDuplicate extends RuntimeException {
    public PasswordsNotDuplicate(String message) {
        super(message);
    }
}
