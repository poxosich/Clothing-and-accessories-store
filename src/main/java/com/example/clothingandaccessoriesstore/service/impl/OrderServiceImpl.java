package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderRequestDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Order;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.exeption.ProductNotFoundException;
import com.example.clothingandaccessoriesstore.exeption.UserNotFoundException;
import com.example.clothingandaccessoriesstore.map.order.OrderMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.BasketRepository;
import com.example.clothingandaccessoriesstore.repository.OrderRepository;
import com.example.clothingandaccessoriesstore.service.BasketService;
import com.example.clothingandaccessoriesstore.service.OrderService;
import com.example.clothingandaccessoriesstore.service.ProductService;
import com.example.clothingandaccessoriesstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final BasketService basketService;
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
        basketRepository.findBasketByProductIdAndUserEmail(orderRequestDto.getProduct().getId(), orderRequestDto.getUser().getEmail())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        try {
            User userByEmail = userService.findUserByEmail(orderRequestDto.getUser().getEmail());
            ProductResponseDto productById = productService.getProductById(orderRequestDto.getProduct().getId());
            if (productById == null) {
                throw new ProductNotFoundException("Product not found");
            }
            Order save = orderRepository.save(Order.builder()
                    .product(productMapper.fromResponseDto(productById))
                    .user(userByEmail)
                    .quantity(orderRequestDto.getQuantity())
                    .totalPrice(orderRequestDto.getTotalPrice())
                    .localDateTime(LocalDateTime.now())
                    .build());
            return orderMapper.toDto(save);
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("User not found");
        }

    }

    @Override
    public void addAllOrder(OrderRequestDto orderRequestDto) {
        try {
            User userByEmail = userService.findUserByEmail(orderRequestDto.getUser().getEmail());
            List<BasketResponseDto> baskedByEmail = basketService.getBaskedByEmail(orderRequestDto.getUser().getEmail());
            if (baskedByEmail != null && !baskedByEmail.isEmpty()) {
                for (BasketResponseDto basketResponseDto : baskedByEmail) {
                    ProductResponseDto productById = productService.getProductById(basketResponseDto.getProduct().getId());
                    orderRepository.save(Order.builder()
                            .product(productMapper.fromResponseDto(productById))
                            .user(userByEmail)
                            .quantity(basketResponseDto.getQuantity())
                            .totalPrice(orderRequestDto.getTotalPrice())
                            .localDateTime(LocalDateTime.now())
                            .build());
                }
            }
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> all = orderRepository.findAll();
        return orderMapper.toDtoList(all);
    }
}
