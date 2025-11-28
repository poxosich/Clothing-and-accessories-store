package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.user.AuthorizationRequest;
import com.example.clothingandaccessoriesstore.dto.user.AuthorizationResponse;
import com.example.clothingandaccessoriesstore.dto.user.UserRequest;
import com.example.clothingandaccessoriesstore.dto.user.UserResponse;
import com.example.clothingandaccessoriesstore.entity.Role;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.exeption.*;
import com.example.clothingandaccessoriesstore.map.user.UserMapper;
import com.example.clothingandaccessoriesstore.repository.UserRepository;
import com.example.clothingandaccessoriesstore.service.UserService;
import com.example.clothingandaccessoriesstore.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MassageService massageService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final String NOT_DUBLIN_PASSWORD = "not dublin Password";
    private static final String USER_DUBLIN = "A person with that name already exists.";
    private static final String MASSAGE_SUBJECT = "Welcome to our online store, please click on this URL %s";
    @Value("${si.massage.active.url}")
    private String url;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        String token = UUID.randomUUID().toString();
        Optional<User> userByEmail = userRepository.findUserByEmail(userRequest.getEmail());
        if (userByEmail.isPresent()) {
            throw new UserDublinException(USER_DUBLIN);
        }
        userRequest.setEmail(userRequest.getEmail());
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRequest.setToken(token);
        userRequest.setRole(Role.USER);
        User savedUser = userRepository.save(userMapper.toRequestEntity(userRequest));
        return userMapper.toDtoResponse(savedUser);
    }


    @Override
    public UserResponse inspectionUserRegister(UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getPasswordDuplicate())) {
            throw new PasswordsNotDuplicate(NOT_DUBLIN_PASSWORD);
        }
        try {
            findUserByEmail(userRequest.getEmail() == null ? null : userRequest.getEmail().trim().toLowerCase());
            throw new UserDublinException(USER_DUBLIN);
        } catch (UserNotFoundException ignored) {
        }
        UserResponse userResponse = saveUser(userRequest);
        massageService.sendMassage(userRequest.getEmail(), MASSAGE_SUBJECT, url + userResponse.getToken());
        return userResponse;
    }

    @Override
    public void findUserByToken(String token) {
        Optional<User> userByToken = userRepository.findUserByToken(token);
        if (userByToken.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        if (userByToken.get().isActive()) {
            new UserResponse();
            return;
        }
        userByToken.get().setActive(true);
        userByToken.get().setToken(null);
        userMapper.toDtoResponse(userRepository.save(userByToken.get()));
    }

    @Override
    public AuthorizationResponse inspectionUserLogin(AuthorizationRequest authorizationRequest) {
        User userByEmail;
        try {
            userByEmail = findUserByEmail(authorizationRequest.getEmail());
            if (!passwordEncoder.matches(authorizationRequest.getPassword(), userByEmail.getPassword())) {
                throw new WrongPasswordException("Wrong password");
            }
            if (!userByEmail.isActive()) {
                throw new EmailNotActiveException("Email not active");
            }
        } catch (UserNotFoundException ignored) {
            throw new UserNotFoundException("User not found");
        }
        return AuthorizationResponse.builder()
                .token(jwtTokenUtil.generateToken(userByEmail.getEmail()))
                .userResponse(userMapper.toDtoResponse(userByEmail))
                .build();
    }
}
