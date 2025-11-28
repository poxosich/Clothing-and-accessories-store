package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.user.AuthorizationRequest;
import com.example.clothingandaccessoriesstore.dto.user.AuthorizationResponse;
import com.example.clothingandaccessoriesstore.dto.user.UserRequest;
import com.example.clothingandaccessoriesstore.dto.user.UserResponse;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.exeption.*;
import com.example.clothingandaccessoriesstore.map.user.UserMapper;
import com.example.clothingandaccessoriesstore.repository.UserRepository;
import com.example.clothingandaccessoriesstore.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserMapper userMapper;
    @Mock
    MassageService massageService;
    @Mock
    JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findUserByEmail() {
        String email = "cholakyanars@gmail.com";
        User user = new User();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        User userByEmail = userService.findUserByEmail(email);

        assertEquals(user, userByEmail);
    }

    @Test
    void saveUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("cholakyanars");
        User user = new User();
        UserResponse userResponse = new UserResponse();

        when(userRepository.findUserByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toRequestEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDtoResponse(user)).thenReturn(userResponse);

        UserResponse actualResponse = userService.saveUser(userRequest);

        assertEquals(userResponse, actualResponse);
    }

    @Test
    void saveUser_UserIsPresent() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("cholakyanars");
        User user = new User();

        when(userRepository.findUserByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserDublinException.class, () -> userService.saveUser(userRequest));
    }

    @Test
    void inspectionUserRegister_passwordsNotMatch() {
        UserRequest request = new UserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123");
        request.setPasswordDuplicate("456");

        assertThrows(PasswordsNotDuplicate.class, () -> userService.inspectionUserRegister(request));
    }


    @Test
    void inspectionUserRegister_success() {
        UserRequest request = new UserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123");
        request.setPasswordDuplicate("123");
        String token = "aaaa";

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        UserResponse response = new UserResponse();
        response.setToken(token);

        when(userMapper.toRequestEntity(any())).thenReturn(new User());
        when(userRepository.save(any())).thenReturn(new User());
        when(userMapper.toDtoResponse(any())).thenReturn(response);

        UserResponse result = userService.inspectionUserRegister(request);

        assertNotNull(result);
        assertEquals("aaaa", result.getToken());
    }

    @Test
    void inspectionUserRegister_UserIsNull() {
        UserRequest request = new UserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123");
        request.setPasswordDuplicate("123");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        assertThrows(UserDublinException.class, () -> userService.inspectionUserRegister(request));
    }

    @Test
    void findUserByToken() {
        String token = "aaaa";
        User user = new User();
        user.setActive(false);
        UserResponse userResponse = new UserResponse();

        when(userRepository.findUserByToken(token)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toDtoResponse(any(User.class))).thenReturn(userResponse);

        userService.findUserByToken(token);

        verify(userRepository, times(1)).findUserByToken(token);
        verify(userRepository, times(1)).save(user); // save should be called
        verify(userMapper, times(1)).toDtoResponse(user);
    }

    @Test
    void findUserByToken_UserIsEmpty() {
        String token = "aaaa";

        when(userRepository.findUserByToken(token)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserByToken(token));
    }

    @Test
    void findUserByToken_UserISActive() {
        String token = "aaaa";
        User user = new User();
        user.setActive(true);

        when(userRepository.findUserByToken(token)).thenReturn(Optional.of(user));

        userService.findUserByToken(token);

        verify(userRepository, times(1)).findUserByToken(token);
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toDtoResponse(any(User.class));
    }


    @Test
    void inspectionUserLogin() {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setEmail("cholakyanars4@gmail.com");
        authorizationRequest.setPassword("123");
        String userPassword = "123";
        User user = new User();
        user.setEmail(authorizationRequest.getEmail());
        user.setActive(true);
        user.setPassword(userPassword);
        UserResponse userResponse = new UserResponse();

        when(userRepository.findUserByEmail(authorizationRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authorizationRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(user.getEmail())).thenReturn("token");
        when(userMapper.toDtoResponse(user)).thenReturn(userResponse);

        AuthorizationResponse actualResponse = userService.inspectionUserLogin(authorizationRequest);
        AuthorizationResponse expectedResponse = AuthorizationResponse.builder()
                .token("token")
                .userResponse(userResponse)
                .build();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void inspectionUserLogin_PasswordNotDuplicate() {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setEmail("cholakyanars4@gmail.com");
        authorizationRequest.setPassword("12");
        String userPassword = "123";
        User user = new User();
        user.setPassword(userPassword);

        when(userRepository.findUserByEmail(authorizationRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authorizationRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(WrongPasswordException.class, () -> userService.inspectionUserLogin(authorizationRequest));
    }

    @Test
    void inspectionUserLogin_UserIsActive() {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setEmail("cholakyanars4@gmail.com");
        authorizationRequest.setPassword("123");
        String userPassword = "123";
        User user = new User();
        user.setActive(false);
        user.setPassword(userPassword);

        when(userRepository.findUserByEmail(authorizationRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authorizationRequest.getPassword(), user.getPassword())).thenReturn(true);

        assertThrows(EmailNotActiveException.class, () -> userService.inspectionUserLogin(authorizationRequest));
    }

    @Test
    void inspectionUserLogin_UserNotFound() {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setEmail("cholakyanars4@gmail.com");
        authorizationRequest.setPassword("123");

        when(userRepository.findUserByEmail(authorizationRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.inspectionUserLogin(authorizationRequest));
    }
}