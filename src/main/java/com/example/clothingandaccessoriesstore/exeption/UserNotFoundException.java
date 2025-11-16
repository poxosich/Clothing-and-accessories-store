package com.example.clothingandaccessoriesstore.exeption;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super(email);
    }
}
