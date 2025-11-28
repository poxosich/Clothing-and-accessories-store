package com.example.clothingandaccessoriesstore.service.impl;

import com.example.clothingandaccessoriesstore.dto.category.CategoryResponseDto;
import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import com.example.clothingandaccessoriesstore.entity.Product;
import com.example.clothingandaccessoriesstore.exeption.CategoryNotFoundException;
import com.example.clothingandaccessoriesstore.exeption.ProductNotFoundException;
import com.example.clothingandaccessoriesstore.map.category.CategoryMapper;
import com.example.clothingandaccessoriesstore.map.product.ProductMapper;
import com.example.clothingandaccessoriesstore.repository.BasketRepository;
import com.example.clothingandaccessoriesstore.repository.LikedRepository;
import com.example.clothingandaccessoriesstore.repository.ProductRepository;
import com.example.clothingandaccessoriesstore.service.CategoryService;
import com.example.clothingandaccessoriesstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private final LikedRepository likedRepository;
    private final BasketRepository basketRepository;
    @Value("${image.package.url}")
    private String path;


    @Override
    public ProductResponseDto addProduct(MultipartFile multipartFile, String name, double price, int categoryId, String description, int quantity) {
        String pictureToFolder = addPictureToFolder(multipartFile);
        CategoryResponseDto categoryById = categoryService.getCategoryById(categoryId);
        Product save = productRepository.save(Product.builder()
                .name(name)
                .price(price)
                .picture(pictureToFolder)
                .category(categoryMapper.toEntity2(categoryById))
                .dateTime(LocalDateTime.now())
                .description(description)
                .quantity(quantity)
                .build());
        return productMapper.toResponseDto(save);

    }

    @Override
    public String addPictureToFolder(MultipartFile multipartFile) {
        String picture;
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new RuntimeException("Is Empty");
        }
        picture = System.currentTimeMillis() + " " + multipartFile.getOriginalFilename();
        File file = new File(path, picture);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return picture;
    }

    @Override
    public List<ProductResponseDto> findTop15ByOrderByIdDesc() {
        return productMapper.toResponseDtoList(productRepository.findTop15ByOrderByIdDesc());
    }

    @Override
    public List<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productMapper.toResponseDtoList(productRepository.findAllByOrderByIdDesc(pageable));
    }

    @Override
    public ProductResponseDto getProductById(Integer id) {
        return productMapper.toResponseDto(productRepository.findById(id).get());
    }

    @Override
    @Transactional
    public void deleteProductById(Integer id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ProductNotFoundException("product not found");
        }
        likedRepository.deleteByProductId(id);
        basketRepository.deleteBasketByProductId(id);
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDto updateProduct(int id, MultipartFile multipartFile, String name, double price, int categoryId, String description, int quantity) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("product not found"));
        CategoryResponseDto categoryById = categoryService.getCategoryById(categoryId);
        if (categoryById == null) {
            throw new CategoryNotFoundException("category not found");
        }
        product.setName(name);
        product.setPrice(price);
        product.setPicture(addPictureToFolder(multipartFile));
        product.setCategory(categoryMapper.toEntity2(categoryById));
        product.setDateTime(LocalDateTime.now());
        product.setDescription(description);
        product.setQuantity(quantity);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    @Override
    public List<ProductResponseDto> getAllProductsByPageable(Pageable pageable) {
        return productMapper.toResponseDtoList(productRepository.findAllByOrderByIdDesc(pageable));
    }

    @Override
    public List<ProductResponseDto> getAllProductsByCategoryId(int categoryId, Pageable pageable) {
        CategoryResponseDto categoryById = categoryService.getCategoryById(categoryId);
        if (categoryById == null) {
            throw new CategoryNotFoundException("category not found");
        }
        List<Product> allByCategoryId = productRepository.findAllByCategoryId(categoryId, pageable);
        return productMapper.toResponseDtoList(allByCategoryId);
    }

    @Override
    public List<ProductResponseDto> getAllProductByName(String productName, Pageable pageable) {
        List<Product> allByName = productRepository.findAllByName(productName, pageable);
        return productMapper.toResponseDtoList(allByName);
    }

}
