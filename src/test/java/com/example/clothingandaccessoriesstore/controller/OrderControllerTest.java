package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.order.OrderRequestDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;
import com.example.clothingandaccessoriesstore.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @Test
    void addOrder() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto();

        String object = objectMapper.writeValueAsString(orderRequestDto);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(object))
                .andExpect(status().isOk());

        verify(orderService, times(1)).addOrder(orderRequestDto);
    }

    @Test
    void addAllOrder() throws Exception {
        int totalPrice = 3;

        mockMvc.perform(post("/order/addAll")
                        .param("totalPrice", String.valueOf(totalPrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).addAllOrder(totalPrice);
    }

    @Test
    void getAllOrders() throws Exception {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        OrderResponseDto orderResponseDto2 = new OrderResponseDto();
        Pageable pageable = PageRequest.of(0, 10);

        when(orderService.getAllOrders(any(Pageable.class)))
                .thenReturn(List.of(orderResponseDto, orderResponseDto2));

        mockMvc.perform(get("/order/getAll")
                        .param("page", pageable.getPageNumber() + "")
                        .param("size", pageable.getPageSize() + "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(orderService, times(1)).getAllOrders(pageable);
    }
}