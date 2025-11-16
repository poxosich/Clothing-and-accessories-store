package com.example.clothingandaccessoriesstore.map.basket;

import com.example.clothingandaccessoriesstore.dto.basket.BasketResponseDto;
import com.example.clothingandaccessoriesstore.entity.Basket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BasketMapper {
    BasketResponseDto toResponseDto(Basket basket);
    List<BasketResponseDto> toResponseDtoList(List<Basket> baskets);
}
