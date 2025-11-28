package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.liked.LikedResponseDto;
import com.example.clothingandaccessoriesstore.service.LikedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LikedControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private LikedService likedService;

    @Test
    void addLiked() throws Exception {
        int productId = 1;
        LikedResponseDto likedResponseDto = new LikedResponseDto();

        when(likedService.add(productId)).thenReturn(likedResponseDto);

        mockMvc.perform(post("/liked")
                        .param("productId", String.valueOf(productId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(likedService, times(1)).add(productId);
    }

    @Test
    void getLiked() throws Exception {
        LikedResponseDto likedResponseDto = new LikedResponseDto();
        LikedResponseDto likedResponseDto2 = new LikedResponseDto();

        when(likedService.getLiked()).thenReturn(List.of(likedResponseDto, likedResponseDto2));

        mockMvc.perform(get("/liked/get").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(likedService, times(1)).getLiked();
    }

    @Test
    void delete() throws Exception {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.delete("/liked/delete")
                        .param("productId", String.valueOf(productId))
                        .param("email", email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(likedService, times(1)).deleteLiked(productId, email);
    }
}