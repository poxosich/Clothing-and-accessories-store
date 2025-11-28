package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.user.AuthorizationRequest;
import com.example.clothingandaccessoriesstore.dto.user.AuthorizationResponse;
import com.example.clothingandaccessoriesstore.dto.user.UserRequest;
import com.example.clothingandaccessoriesstore.dto.user.UserResponse;
import com.example.clothingandaccessoriesstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void register() throws Exception {
        UserRequest  userRequest = new UserRequest();
        UserResponse userResponse = new UserResponse();

        String object = objectMapper.writeValueAsString(userRequest);

        when(userService.inspectionUserRegister(userRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object))
                .andExpect(status().isOk());

        verify(userService, times(1)).inspectionUserRegister(userRequest);
    }

    @Test
    void confirmation() throws Exception {
        String token = "token";

        mockMvc.perform(get("/user/confirmation")
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:4200/login"));

        verify(userService, times(1)).findUserByToken(token);
    }

    @Test
    void login() throws Exception {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        AuthorizationResponse authorizationResponse = new AuthorizationResponse();

        when(userService.inspectionUserLogin(authorizationRequest)).thenReturn(authorizationResponse);

        String object = objectMapper.writeValueAsString(authorizationRequest);

        mockMvc.perform(post("/user/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object))
                .andExpect(status().isOk());

        verify(userService, times(1)).inspectionUserLogin(authorizationRequest);
    }
}