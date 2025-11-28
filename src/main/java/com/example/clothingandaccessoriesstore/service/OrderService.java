package com.example.clothingandaccessoriesstore.service;

import com.example.clothingandaccessoriesstore.dto.order.OrderRequestDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDto addOrder(OrderRequestDto orderRequestDto);
    void addAllOrder(int totalPrice);
    List<OrderResponseDto> getAllOrders(Pageable pageable);
}
