package com.example.clothingandaccessoriesstore.map.order;

import com.example.clothingandaccessoriesstore.dto.order.OrderResponseDto;
import com.example.clothingandaccessoriesstore.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDto toDto(Order order);
    List<OrderResponseDto> toDtoList(List<Order> orders);
}
