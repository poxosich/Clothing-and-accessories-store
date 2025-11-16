package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.category.CategoryRequestDto;
import com.example.clothingandaccessoriesstore.dto.category.CategoryResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Category;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.exeption.CategoryDuplicateException;
import com.example.clothingandaccessoriesstore.exeption.CategoryNotFoundException;
import com.example.clothingandaccessoriesstore.map.category.CategoryMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.CategoryRepository;
import com.example.clothingandaccessoriesstore.repository.ProductRepository;
import com.example.clothingandaccessoriesstore.service.CategoryService;
import com.example.clothingandaccessoriesstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;


    @Override
    public CategoryResponseDto saveCategory(CategoryRequestDto categoryRequestDto) {
        Optional<Category> categoryByName = categoryRepository.findCategoryByName(categoryRequestDto.getName());
        if (categoryByName.isPresent()) {
            throw new CategoryDuplicateException(categoryRequestDto.getName() + "already exists");
        }
        Category save = categoryRepository.save(categoryMapper.toEntity(categoryRequestDto));
        return categoryMapper.toDtoResponse(save);
    }

    @Override
    public void deleteCategoryById(int id) {
        Optional<Category> byId = categoryRepository.findById(id);
        if (byId.isEmpty()) {
            throw new CategoryNotFoundException("Category" + id + " not found");
        }
        List<Product> productByCategoryId = productRepository.findProductByCategory_Id(id);
        for (Product product : productByCategoryId) {
            product.setCategory(null);
        }
        categoryRepository.deleteById(byId.get().getId());
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryMapper.dtoResponseList(categoryRepository.findAll());
    }

    @Override
    public CategoryResponseDto getCategoryById(int id) {
        Optional<Category> byId = categoryRepository.findById(id);
        if (byId.isEmpty()) {
            throw new CategoryNotFoundException("Category with id " + id + " not found");
        }
        return categoryMapper.toDtoResponse(byId.get());
    }
}
