package com.example.clothingandaccessoriesstore.map.product;

import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);
    List<ProductResponseDto> toResponseDtoList(List<Product> products);
    Product fromResponseDto(ProductResponseDto productResponseDto);

}
