package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.basket.BasketRequestDto;
import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Basket;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.map.basket.BasketMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.BasketRepository;
import com.example.clothingandaccessoriesstore.service.BasketService;
import com.example.clothingandaccessoriesstore.service.ProductService;
import com.example.clothingandaccessoriesstore.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceImplTest {
    @Mock
    private BasketRepository basketRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private BasketMapper basketMapper;

    @InjectMocks
    private BasketServiceImpl basketService;

    @Test
    void addBasket() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";
        User user = new User();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        Product product = new Product();
        Basket savedBasket = new Basket();
        BasketResponseDto expectedResponse = new BasketResponseDto();

        when(basketRepository.findBasketByProductIdAndUserEmail(productId, email)).thenReturn(Optional.empty());
        when(userService.findUserByEmail(email)).thenReturn(user);
        when(productService.getProductById(productId)).thenReturn(productResponseDto);
        when(productMapper.fromResponseDto(productResponseDto)).thenReturn(product);
        when(basketRepository.save(any(Basket.class))).thenReturn(savedBasket);
        when(basketMapper.toResponseDto(savedBasket)).thenReturn(expectedResponse);
        BasketResponseDto response = basketService.addBasket(productId, email);

        assertThat(response).isSameAs(expectedResponse);
    }


    @Test
    void getBaskedByEmail() {
        String email = "cholakyanars4@gmail.com";
        List<BasketResponseDto> response = List.of(new BasketResponseDto(),  new BasketResponseDto());
        List<Basket> baskets = List.of(new Basket(),  new Basket());
        when(basketRepository.findBasketByUserEmail(email)).thenReturn(baskets);
        when(basketMapper.toResponseDtoList(baskets)).thenReturn(response);
        assertThat(basketService.getBaskedByEmail(email)).isEqualTo(response);
    }


    @Test
    void deleteBasket() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";
        ProductResponseDto productResponseDto = new ProductResponseDto();
        when(userService.findUserByEmail(email)).thenReturn(new  User());
        when(productService.getProductById(productId)).thenReturn(productResponseDto);
        basketService.deleteBasket(productId, email);
        verify(basketRepository).deleteBasketByProductIdAndUserEmail(productId, email);
    }


    @Test
    void updateBasket() {
        int productId = 1;
        String email = "cholakyanars4@gmail.com";

        BasketRequestDto requestDto = new BasketRequestDto();
        Product product = new Product();
        product.setId(productId);
        User user = new User();
        user.setEmail(email);

        requestDto.setProduct(product);
        requestDto.setUser(user);
        requestDto.setQuantity(5);

        Basket existingBasket = new Basket();

        when(basketRepository.findBasketByProductIdAndUserEmail(requestDto.getProduct().getId(), requestDto.getUser().getEmail())).thenReturn(Optional.of(existingBasket));
        existingBasket.setQuantity(4);
        basketService.updateBasket(requestDto);
        verify(basketRepository).save(existingBasket);    }
}
