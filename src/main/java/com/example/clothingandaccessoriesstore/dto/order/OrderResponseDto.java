package com.example.clothingandaccessoriesstore.dto.order;

import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private int id;
    private Product product;
    private User user;
    private int quantity;
    private int totalPrice;
    private LocalDateTime localDateTime;
}
