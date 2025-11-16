package com.example.clothingandaccessoriesstore.service;

import com.example.clothingandaccessoriesstore.dto.order.OrderRequestDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto addOrder(OrderRequestDto orderRequestDto);
    void addAllOrder(OrderRequestDto orderRequestDto);
    List<OrderResponseDto> getAllOrders();
}
