package com.example.clothingandaccessoriesstore.service;

import com.example.clothingandaccessoriesstore.dto.product.ProductResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
     ProductResponseDto addProduct(MultipartFile multipartFile, String name, double price, int categoryId, String description, int quantity);
     String addPictureToFolder(MultipartFile multipartFile);
     List<ProductResponseDto> findTop15ByOrderByIdDesc();
     List<ProductResponseDto> getAllProducts(Pageable pageable);
     ProductResponseDto getProductById(Integer id);
     void deleteProductById(Integer id);
     ProductResponseDto updateProduct(int id, MultipartFile multipartFile, String name, double price, int categoryId, String description, int quantity);
     List<ProductResponseDto> getAllProductsByPageable(Pageable pageable);
     List<ProductResponseDto> getAllProductsByCategoryId(int categoryId, Pageable pageable);
     List<ProductResponseDto> getAllProductByName(String productName, Pageable pageable);
}
