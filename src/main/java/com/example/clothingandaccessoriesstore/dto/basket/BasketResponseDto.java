package com.example.clothingandaccessoriesstore.dto.basket;

import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketResponseDto {
    private int id;
    private Product product;
    private User user;
    private int quantity;
}
