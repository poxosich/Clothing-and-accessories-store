package com.example.clothingandaccessoriesstore.service;

import com.example.clothingandaccessoriesstore.dto.category.CategoryRequestDto;
import com.example.clothingandaccessoriesstore.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto saveCategory(CategoryRequestDto categoryRequestDto);
    void deleteCategoryById(int id);
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(int id);
}
