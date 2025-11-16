package com.example.clothingandaccessoriesstore.map.category;

import com.example.clothingandaccessoriesstore.dto.category.CategoryRequestDto;
import com.example.clothingandaccessoriesstore.dto.category.CategoryResponseDto;
import com.example.clothingandaccessoriesstore.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto toDtoResponse(Category category);
    List<CategoryResponseDto> dtoResponseList(List<Category> categories);
    Category toEntity2(CategoryResponseDto categoryResponseDto);
}
