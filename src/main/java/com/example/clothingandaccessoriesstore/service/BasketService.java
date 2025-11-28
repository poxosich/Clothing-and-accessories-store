package com.example.clothingandaccessoriesstore.service;

import com.example.clothingandaccessoriesstore.dto.basket.BasketRequestDto;
import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;

import java.util.List;

public interface BasketService {
    BasketResponseDto addBasket(int productid);
    List<BasketResponseDto> getBaskedByEmail();
    void deleteBasket(int productid, String email);
    void updateBasket(BasketRequestDto requestDto);
}
