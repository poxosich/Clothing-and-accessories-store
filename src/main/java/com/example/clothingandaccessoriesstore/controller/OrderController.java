package com.example.clothingandaccessoriesstore.controller;

import com.example.clothingandaccessoriesstore.dto.order.OrderRequestDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;
import com.example.clothingandaccessoriesstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    public void addOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.addOrder(orderRequestDto);
    }

    @PostMapping("/addAll")
    public void addAllOrder(@RequestParam int totalPrice) {
        orderService.addAllOrder(totalPrice);
    }

    @GetMapping("/getAll")
    public List<OrderResponseDto> getAllOrders(@PageableDefault(size = 3) Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }
}
