package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.basket.BasketRequestDto;
import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.service.BasketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BasketControllerTest {

    @MockBean
    BasketService basketService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addBasket() throws Exception {
        Integer productid = 1;
        BasketResponseDto responseDto = new BasketResponseDto();

        when(basketService.addBasket(productid)).thenReturn(responseDto);

        mockMvc.perform(post("/basket")
                        .param("productid", productid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(basketService, times(1)).addBasket(productid);
    }


    @Test
    void getBasket() throws Exception {
        BasketResponseDto responseDto = new BasketResponseDto();
        BasketResponseDto responseDto2 = new BasketResponseDto();

        when(basketService.getBaskedByEmail()).thenReturn(List.of(responseDto, responseDto2));

        mockMvc.perform(get("/basket/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBasket() throws Exception {
        int productid = 1;
        String email = "cholakyanars4@gmail.com";
        basketService.deleteBasket(productid, email);

        mockMvc.perform(delete("/basket/delete")
                        .param("productId", String.valueOf(productid))
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateBasket() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("cholakyanars");
        product.setDescription("cholakyanars");
        BasketRequestDto basketRequestDto = new BasketRequestDto();
        basketRequestDto.setProduct(product);
        basketRequestDto.setQuantity(3);
        String basketJson = objectMapper.writeValueAsString(basketRequestDto);

        mockMvc.perform(put("/basket/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketJson))
                .andExpect(status().isOk());

        verify(basketService, times(1)).updateBasket(any(BasketRequestDto.class));
    }
}