package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.basket.BasketRequestDto;
import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Basket;
import com.example.clothingandaccessoriesstore.entity.User;
import com.example.clothingandaccessoriesstore.exeption.BasketDuplicateException;
import com.example.clothingandaccessoriesstore.exeption.ProductNotFoundException;
import com.example.clothingandaccessoriesstore.exeption.UserNotFoundException;
import com.example.clothingandaccessoriesstore.map.basket.BasketMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.BasketRepository;
import com.example.clothingandaccessoriesstore.service.BasketService;
import com.example.clothingandaccessoriesstore.service.ProductService;
import com.example.clothingandaccessoriesstore.service.UserService;
import com.example.clothingandaccessoriesstore.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final UserService userService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public BasketResponseDto addBasket(int productid) {
        String email = jwtTokenUtil.getCurrentUserEmail();
        Optional<Basket> basketByProductIdAndUserEmail = basketRepository.findBasketByProductIdAndUserEmail(productid, email);
        if (basketByProductIdAndUserEmail.isPresent()) {
            throw new BasketDuplicateException("Basket already exists");
        }
        try {
            User userByEmail = userService.findUserByEmail(email);
            ProductResponseDto productById = productService.getProductById(productid);
            if (productById == null) {
                throw new ProductNotFoundException("Product not found");
            }
            Basket save = basketRepository.save(Basket.builder()
                    .product(productMapper.fromResponseDto(productById))
                    .user(userByEmail)
                    .quantity(1)
                    .build());
            return basketMapper.toResponseDto(save);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<BasketResponseDto> getBaskedByEmail() {
        String email = jwtTokenUtil.getCurrentUserEmail();
        return basketMapper.toResponseDtoList(basketRepository.findBasketByUserEmail(email));
    }

    @Transactional
    @Override
    public void deleteBasket(int productid, String email) {
        try {
            userService.findUserByEmail(email);
            ProductResponseDto productById = productService.getProductById(productid);
            if (productById == null) {
                throw new ProductNotFoundException("Product not found");
            }
            basketRepository.deleteBasketByProductIdAndUserEmail(productid, email);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public void updateBasket(BasketRequestDto requestDto) {
        Basket basket = basketRepository.findBasketByProductIdAndUserEmail(requestDto.getProduct().getId(), requestDto.getUser().getEmail())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        basket.setQuantity(requestDto.getQuantity());
        basketRepository.save(basket);
    }
}
