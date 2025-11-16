package com.example.clothingandaccessoriesstore.cantroller;

import com.example.clothingandaccessoriesstore.dto.order.OrderRequestDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;
import com.example.clothingandaccessoriesstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/add")
    public void addOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.addOrder(orderRequestDto);
    }

    @PostMapping("/addAll")
    public void addAllOrder(@RequestBody OrderRequestDto orderRequestDto) {
       orderService.addAllOrder(orderRequestDto);
    }

    @GetMapping("/getAll")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}
