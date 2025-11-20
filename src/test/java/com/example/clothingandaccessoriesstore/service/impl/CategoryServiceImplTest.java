package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.category.CategoryRequestDto;
import com.example.clothingandaccessoriesstore.dto.category.CategoryResponseDto;
import com.example.clothingandaccessoriesstore.entity.Category;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.map.category.CategoryMapper;
import com.example.clothingandaccessoriesstore.repository.CategoryRepository;
import com.example.clothingandaccessoriesstore.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryMapper categoryMapper;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    CategoryServiceImpl categoryService;


    @Test
    void saveCategory() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("fffff");
        categoryRequestDto.setDescription("fffff");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        Category category = new Category();

        when(categoryRepository.findCategoryByName(categoryRequestDto.getName())).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDtoResponse(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto categoryResponseDtoService = categoryService.saveCategory(categoryRequestDto);
        assertThat(categoryResponseDtoService).isSameAs(categoryResponseDto);
    }


    @Test
    void deleteCategoryById() {
        int categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        Product product = new Product();
        Product product2 = new Product();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findProductByCategory_Id(categoryId)).thenReturn(List.of(product, product2));

        assertNull(product.getCategory());
        assertNull(product2.getCategory());

        categoryService.deleteCategoryById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }


    @Test
    void getAllCategories() {
        Category category = new Category();
        Category category2 = new Category();
        CategoryResponseDto dto1 = new CategoryResponseDto();
        CategoryResponseDto dto2 = new CategoryResponseDto();

        when(categoryRepository.findAll()).thenReturn(List.of(category, category2));
        when(categoryMapper.dtoResponseList(List.of(category, category2))).thenReturn(List.of(dto1, dto2));

        List<CategoryResponseDto> allCategories = categoryService.getAllCategories();

        assertThat(allCategories).isEqualTo(List.of(dto1, dto2));
    }


    @Test
    void getCategoryById() {
        int categoryId = 1;
        Category category = new Category();
        CategoryResponseDto dto = new CategoryResponseDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDtoResponse(category)).thenReturn(dto);

        CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(categoryId);

        assertThat(categoryResponseDto).isSameAs(dto);
    }
}