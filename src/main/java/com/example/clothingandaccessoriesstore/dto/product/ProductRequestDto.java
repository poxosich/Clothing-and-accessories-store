package com.example.clothingandaccessoriesstore.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    private String name;
    private double price;
    private MultipartFile multipartFile;
    private String category;
    private String description;
    private int quantity;
}
