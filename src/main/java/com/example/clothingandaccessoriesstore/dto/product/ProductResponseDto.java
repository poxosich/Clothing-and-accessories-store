package com.example.clothingandaccessoriesstore.dto.product;

import com.example.clothingandaccessoriesstore.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
    private Integer id;
    private String name;
    private double price;
    private String picture;
    private Category category;
    private LocalDateTime dateTime;
    private boolean active;
    private String description;
    private int quantity;
}
