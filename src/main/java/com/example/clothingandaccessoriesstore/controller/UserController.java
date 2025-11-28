package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.user.AuthorizationRequest;
import com.example.clothingandaccessoriesstore.dto.user.AuthorizationResponse;
import com.example.clothingandaccessoriesstore.dto.user.UserRequest;
import com.example.clothingandaccessoriesstore.dto.user.UserResponse;
import com.example.clothingandaccessoriesstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.inspectionUserRegister(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/confirmation")
    public ResponseEntity<Void> confirmation(@RequestParam String token) {
        userService.findUserByToken(token);
        URI redirectUri = URI.create("http://localhost:4200/login");
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }

    @PostMapping("/authorization")
    public ResponseEntity<AuthorizationResponse> login(@RequestBody AuthorizationRequest authorizationRequest) {
        AuthorizationResponse authorizationResponse = userService.inspectionUserLogin(authorizationRequest);
        return ResponseEntity.ok(authorizationResponse);
    }
}
