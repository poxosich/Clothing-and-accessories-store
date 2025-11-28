package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderRequestDto;
import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Basket;
import com.example.clothingandaccessoriesstore.entity.Order;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.exeption.ProductNotFoundException;
import com.example.clothingandaccessoriesstore.exeption.UserNotFoundException;
import com.example.clothingandaccessoriesstore.map.order.OrderMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.BasketRepository;
import com.example.clothingandaccessoriesstore.repository.OrderRepository;
import com.example.clothingandaccessoriesstore.service.BasketService;
import com.example.clothingandaccessoriesstore.service.ProductService;
import com.example.clothingandaccessoriesstore.service.UserService;
import com.example.clothingandaccessoriesstore.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    BasketService basketService;
    @Mock
    BasketRepository basketRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    UserService userService;
    @Mock
    ProductService productService;
    @Mock
    ProductMapper productMapper;
    @Mock
    OrderMapper orderMapper;
    @Mock
    JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void addOrder() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        Product product = new Product();
        product.setId(1);
        orderRequestDto.setProduct(product);
        User userReq = new User();
        userReq.setEmail("arsencholakyan4@gmail.com");
        orderRequestDto.setUser(userReq);
        Basket basket = new Basket();
        User user = new User();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        Order order = new Order();
        OrderResponseDto orderResponseDto = new OrderResponseDto();

        when(basketRepository.findBasketByProductIdAndUserEmail(orderRequestDto.getProduct().getId(), orderRequestDto.getUser().getEmail())).thenReturn(Optional.of(basket));
        when(userService.findUserByEmail(orderRequestDto.getUser().getEmail())).thenReturn(user);
        when(productService.getProductById(orderRequestDto.getProduct().getId())).thenReturn(productResponseDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderService.addOrder(orderRequestDto);

        verify(orderRepository).save(any(Order.class));

        assertThat(result).isEqualTo(orderResponseDto);
    }

    @Test
    void addOrderProductIsNull() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        Product product = new Product();
        product.setId(1);
        orderRequestDto.setProduct(product);
        User user = new User();
        user.setEmail("arsencholakyan4@gmail.com");
        orderRequestDto.setUser(user);
        User userResponse = new User();

        when(basketRepository.findBasketByProductIdAndUserEmail(orderRequestDto.getProduct().getId(), orderRequestDto.getUser().getEmail())).thenReturn(Optional.of(new Basket()));
        when(userService.findUserByEmail(orderRequestDto.getUser().getEmail())).thenReturn(userResponse);
        when(productService.getProductById(orderRequestDto.getProduct().getId())).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> orderService.addOrder(orderRequestDto));
    }

    @Test
    void addOrderUserNotFound() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        Product product = new Product();
        product.setId(1);
        orderRequestDto.setProduct(product);
        User user = new User();
        user.setEmail("arsencholakyan4@gmail.com");
        orderRequestDto.setUser(user);

        when(basketRepository.findBasketByProductIdAndUserEmail(product.getId(), user.getEmail())).thenReturn(Optional.of(new Basket()));
        when(userService.findUserByEmail(user.getEmail())).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> orderService.addOrder(orderRequestDto));
    }

    @Test
    void addAllOrder() {
        int totalPrice = 444;
        String email = "cholakyanars4@gmail.com";
        User user = new User();
        Product  product = new Product();
        product.setId(1);
        Product  product2 = new Product();
        product2.setId(2);
        BasketResponseDto basketResponseDto = new BasketResponseDto();
        basketResponseDto.setProduct(product);
        basketResponseDto.setQuantity(2);
        BasketResponseDto basketResponseDto2 = new BasketResponseDto();
        basketResponseDto2.setProduct(product2);
        basketResponseDto2.setQuantity(5);
        ProductResponseDto productResponseDto = new ProductResponseDto();
        Order order = new Order();

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(userService.findUserByEmail(email)).thenReturn(user);
        when(basketService.getBaskedByEmail()).thenReturn(List.of(basketResponseDto,  basketResponseDto2));
        when(productService.getProductById(anyInt())).thenReturn(productResponseDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.addAllOrder(totalPrice);

        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void addAllOrder_BasketIsNull() {
        String email  = "cholakyanars4@gmail.com";
        User userResponse = new User();

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(userService.findUserByEmail(email)).thenReturn(userResponse);
        when(basketService.getBaskedByEmail()).thenReturn(null);

        orderService.addAllOrder(444);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void addAllOrderProductListIsEmpty() {
        String email  = "cholakyanars4@gmail.com";
        User userResponse = new User();

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(userService.findUserByEmail(email)).thenReturn(userResponse);
        when(basketService.getBaskedByEmail()).thenReturn(Collections.emptyList());

        orderService.addAllOrder(444);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void addAllOrderUserNotFound() {
        String email  = "cholakyanars4@gmail.com";

        when(jwtTokenUtil.getCurrentUserEmail()).thenReturn(email);
        when(userService.findUserByEmail(email)).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> orderService.addAllOrder(444));
    }

    @Test
    void getAllOrders() {
        Order order = new Order();
        Order order2 = new Order();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        OrderResponseDto orderResponseDto2 = new OrderResponseDto();
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findAllByOrderByIdDesc(pageable)).thenReturn(List.of(order, order2));
        when(orderMapper.toDtoList(List.of(order, order2))).thenReturn(List.of(orderResponseDto, orderResponseDto2));

        List<OrderResponseDto> allOrders = orderService.getAllOrders(pageable);

        assertThat(allOrders).isEqualTo(List.of(orderResponseDto, orderResponseDto2));
    }
}