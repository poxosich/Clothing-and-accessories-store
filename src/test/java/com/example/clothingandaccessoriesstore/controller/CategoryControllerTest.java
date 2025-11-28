package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.category.CategoryRequestDto;
import com.example.clothingandaccessoriesstore.dto.category.CategoryResponseDto;
import com.example.clothingandaccessoriesstore.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addCategory() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("name");
        categoryRequestDto.setDescription("description");
        CategoryResponseDto responseDto = new CategoryResponseDto();

        when(categoryService.saveCategory(any(CategoryRequestDto.class))).thenReturn(responseDto);

        String requestJson = objectMapper.writeValueAsString(categoryRequestDto);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategoryById() throws Exception {
        int id = 1;

        mockMvc.perform(delete("/category/delete/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategoryById(id);
    }

    @Test
    void getAllCategories() throws Exception {
        CategoryResponseDto responseDto = new CategoryResponseDto();
        CategoryResponseDto responseDto2 = new CategoryResponseDto();

        when(categoryService.getAllCategories()).thenReturn(List.of(responseDto, responseDto2));

        mockMvc.perform(get("/category/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getAllCategories();
    }
}