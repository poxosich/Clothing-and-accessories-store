package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @Test
    void addProduct() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("picture", "test image content".getBytes());
        String name = "name1";
        double price = 10.00;
        int categoryId = 1;
        String description = "description1";
        int quantity = 1;
        ProductResponseDto productResponseDto = new ProductResponseDto();

        when(productService.addProduct(multipartFile, name, price, categoryId, description, quantity)).thenReturn(productResponseDto);

        mockMvc.perform(multipart("/product")
                        .file(multipartFile)
                        .param("name", name)
                        .param("price", String.valueOf(price))
                        .param("categoryId", String.valueOf(categoryId))
                        .param("description", description)
                        .param("quantity", String.valueOf(quantity))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());

        verify(productService, times(1)).addProduct(multipartFile, name, price, categoryId, description, quantity);
    }

    @Test
    void get15Product() throws Exception {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();

        when(productService.findTop15ByOrderByIdDesc()).thenReturn(List.of(productResponseDto, productResponseDto2));

        mockMvc.perform(get("/product/get15Product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).findTop15ByOrderByIdDesc();
    }

    @Test
    void getAllProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 3);
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();

        when(productService.getAllProducts(pageable)).thenReturn(List.of(productResponseDto, productResponseDto2));

        mockMvc.perform(get("/product/grtAll")
                        .param("page", pageable.getPageNumber() + "")
                        .param("size", pageable.getPageSize() + "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAllProducts(pageable);
    }

    @Test
    void getById() throws Exception {
        int id = 1;
        ProductResponseDto productResponseDto = new ProductResponseDto();

        when(productService.getProductById(id)).thenReturn(productResponseDto);

        mockMvc.perform(get("/product/getById/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getProductById(id);
    }

    @Test
    void updateProduct() throws Exception {
        int id = 1;
        MockMultipartFile multipartFile = new MockMultipartFile("picture", "test image content".getBytes());
        String name = "updatedName";
        double price = 99.99;
        int categoryId = 2;
        String description = "updated description";
        int quantity = 10;
        ProductResponseDto productResponseDto = new ProductResponseDto();

        when(productService.updateProduct(id, multipartFile, name, price, categoryId, description, quantity)).thenReturn(productResponseDto);

        mockMvc.perform(multipart("/product/update")
                        .file(multipartFile)
                        .param("id", String.valueOf(id))
                        .param("name", name)
                        .param("price", String.valueOf(price))
                        .param("categoryId", String.valueOf(categoryId))
                        .param("description", description)
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(productService, times(1)).updateProduct(id, multipartFile, name, price, categoryId, description, quantity);
    }

    @Test
    void getAllByPageable() throws Exception {
        Pageable pageable = PageRequest.of(0, 3);
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();

        when(productService.getAllProductsByPageable(pageable)).thenReturn(List.of(productResponseDto, productResponseDto2));

        mockMvc.perform(get("/product/getAllByPageable")
                        .param("page", pageable.getPageNumber() + "")
                        .param("size", pageable.getPageSize() + "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAllProductsByPageable(pageable);
    }

    @Test
    void getCategoryById() throws Exception {
        int id = 1;
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();
        Pageable pageable = PageRequest.of(0, 3);

        when(productService.getAllProductsByCategoryId(id, pageable)).thenReturn(List.of(productResponseDto, productResponseDto2));

        mockMvc.perform(get("/product/category/{id}", id)
                        .param("page", pageable.getPageNumber() + "")
                        .param("size", pageable.getPageSize() + "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAllProductsByCategoryId(id, pageable);
    }

    @Test
    void getProductByName() throws Exception {
        String name = "name";
        ProductResponseDto productResponseDto = new ProductResponseDto();
        ProductResponseDto productResponseDto2 = new ProductResponseDto();
        Pageable pageable = PageRequest.of(0, 3);

        when(productService.getAllProductByName(name, pageable)).thenReturn(List.of(productResponseDto, productResponseDto2));

        mockMvc.perform(get("/product/productBy/{name}", name)
                        .param("page", pageable.getPageNumber() + "")
                        .param("size", pageable.getPageSize() + "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAllProductByName(name, pageable);
    }
}